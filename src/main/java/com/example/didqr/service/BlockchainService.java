package com.example.didqr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    /*
    [ 블록체인 문서 등록 ]

    1. Solidity 함수 호출 데이터("registerDocument") 인코딩
    2. 지갑의 nonce를 가져온다
    3. gasPrice, gasLimit을 정한다
    4. 트랜잭션을 만든다
    5. private key로 서명한다
    6. Ganache에 전송한다
    7. txHash를 반환한다
    */
    public String registerDocument(Long documentId, String issuerDid, String contentHash) {
        try {
            Function function = new Function(
                    // 함수 이름
                    "registerDocument",

                    // 함수 입력 파라미터
                    Arrays.asList(
                            new Uint256(BigInteger.valueOf(documentId)),
                            new Utf8String(issuerDid),
                            new Utf8String(contentHash)
                    ),

                    // 함수 반환 타입 정의 (없음)
                    Collections.emptyList()
            );

            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount =
                    web3j.ethGetTransactionCount(
                            credentials.getAddress(),
                            DefaultBlockParameterName.LATEST
                    ).send();

            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
            BigInteger gasLimit = BigInteger.valueOf(3_000_000);

            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    gasLimit,
                    contractAddress,
                    BigInteger.ZERO,
                    encodedFunction
            );

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction response = web3j.ethSendRawTransaction(hexValue).send();

            if (response.hasError()) {
                throw new RuntimeException(response.getError().getMessage());
            }

            return response.getTransactionHash();

        } catch (Exception e) {
            throw new RuntimeException("블록체인 문서 등록 실패", e);
        }
    }

    /*
    [ 블록체인 문서 해시 조회 ]

    1. Solidity 함수 호출 데이터("getDocument") 인코딩
    2. eth_call 방식으로 블록체인에 조회 요청
       (트랜잭션 생성 X, gas 소비 X)

    3. 스마트컨트랙트가 반환한 데이터 디코딩
       return 값:
       - issuerDid
       - contentHash
       - issuedAt
       - issuer address

    4. 반환값 중 contentHash 추출
    5. 검증용 블록체인 해시 반환
    */
    public String getContentHash(Long documentId) {
        try {
            Function function = new Function(
                    // 함수 이름
                    "getDocument",

                    // 함수 입력 파라미터
                    Arrays.asList(new Uint256(BigInteger.valueOf(documentId))),

                    // 함수 반환 타입 정의
                    Arrays.asList(
                            new TypeReference<Utf8String>() {}, // issuerDid
                            new TypeReference<Utf8String>() {}, // contentHash
                            new TypeReference<Uint256>() {},    // issuedAt
                            new TypeReference<Address>() {}     // issuer
                    )
            );

            String encodedFunction = FunctionEncoder.encode(function);

            EthCall response = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            credentials.getAddress(),
                            contractAddress,
                            encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send();

            List<Type> result = FunctionReturnDecoder.decode(
                    response.getValue(),
                    function.getOutputParameters()
            );

            return result.get(1).getValue().toString();

        } catch (Exception e) {
            throw new RuntimeException("블록체인 문서 해시 조회 실패", e);
        }
    }
}
