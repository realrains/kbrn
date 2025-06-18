/**
 * Jackson 직렬화/역직렬화 지원 모듈
 * <p>
 * 이 패키지는 Jackson 라이브러리를 사용하여 KBRN 객체를 JSON으로 직렬화하고 
 * JSON에서 KBRN 객체로 역직렬화하는 기능을 제공합니다.
 * 
 * <p>사용 예시:
 * <pre>{@code
 * ObjectMapper mapper = new ObjectMapper();
 * mapper.registerModule(new KbrnModule());
 * 
 * // 직렬화
 * KBRN kbrn = KBRN.valueOf("120-81-47521");
 * String json = mapper.writeValueAsString(kbrn); // "120-81-47521"
 * 
 * // 역직렬화
 * KBRN deserialized = mapper.readValue("\"1208147521\"", KBRN.class);
 * }</pre>
 * 
 * @see io.github.realrain.kbrn.jackson.KbrnModule
 * @see io.github.realrain.kbrn.jackson.KbrnSerializer
 * @see io.github.realrain.kbrn.jackson.KbrnDeserializer
 */
package io.github.realrain.kbrn.jackson;