놀랍게도  **‘클린 코드를 위해 Junit 이라는 테스트 프레임워크 를  어떻게 사용할 것인가’ 에 대한 내용이 *아니**다*

Junit 의 시작을 함께한 켄트 백과, 에릭감마가 초기에 작성한 ComparisonCompactor 라는 모듈을 개선시키는 내용에 대한 챕터였다.

보이스카우트 규칙에 따라 , 내가 손 대는 코드를 조금이라도 더 클린하게 개선시켜 나가는 과정에 대한 이야기다.

14장 처럼 코드를 개선시켜나가는 과정을 쓱 보여주어서 책을 직접 읽는게 좋을 것 같지만

정리해 보자면

- 예전 코드의 잔재 : 범위(scope) 을 변수명에 prefix 로 붙인 것 제거하기
    - 멤버 변수들에 f 라는 prefix 가 붙어있던 것들 제거
- 조건문 캡슐화 : 여러 일을 하고 있는 함수 내부에서 if 조건문이 ‘어떤 것을 확인’ 하고 있는지 ‘의도를 명확히 표현’ 하도록 메서드 추출
- this.변수_이름 ..안 써두 되지 않니? → 멤버 변수랑 함수 내부의 변수는 분명 다른 의미를 갖고 있을 걸 ? 이름을 명확하게 붙여주자.
- 부정문 은 긍정문 보다 이해하기가 약간 더 어려움
    - (내 생각) 개인적으로는 else 문이 생기는게 더 싫긴 한데.. 책에서는 ‘긍정문’ 으로 바꾸는 과정에서 else 문을 사용하게 됨.

    ```java
    public String compact(String message) {
    	if ( shouldNotCompact()) {
    		return ..// compact 안하고 return 하는 로직
    	}
    	// compact 로직
    	return ..
    }
    ```

    ```java
    
    public String compact(String message) {
    	if (canBeCompacted()) {
    		// compact 로직
    		return ..
    	} else {
    			return ..// compact 안하고 return 하는 로직
    	}
    }
    
    ```

- A, B 라는 ‘여러 일’ 을 하고 있는 함수의 이름이 B 만을 나타내고 있는 경우 → A,B 모두를 해야 하는 함수라면, 두 작업 모두를 하는 함수임을 나타내는 이름으로!
    - 가장 흔한 경우 : 오류 점검 이라는 부가 단계가 포함되어 있는데, 이 점검 부분은 전혀 나타내지 않는 경우
        - compact 는 내부에서, canBeCompacted 라는 점검 단계를 전혀 나타내지 못하는 이름.
    - 책 : compact 내부에서는 A. 형식이 갖춰진 문자열로 B.압축 을 하고 있음 → formatCompactedComparision 이 필요
- formatCompactedComparison 에서 메서드 추출 해 보자 :
    - format (형식을 맞추는) 작업은 여기서 하고,
    - 압축 작업은 또 빼낼 수도 있음 → compactExpectedAndActual();
- ‘**함수 로컬 변수’ 이던 부분을 → ‘멤버 변수’로 승격** 하자
    - formatCompactedComparison 에서 compactExpectedAndActual 메서드 추출하는 과정에서, formatCompactedComparison 에서는  지역변수이던 부분을, 멤버 변수로 승격 → compactExpectedAndActual 에서는 멤버 변수에 값을 할당하는 작업을 함.
    - ( 내 생각) 아마, 함수 로컬 변수로 **그대로 두었다면, compactExpectedAndActual 는 2개 이상의 인수를 받는 함수가 되었을 것.** 클린코드에서는 함수가 인자를 갖지 않거나 최대 1,2개 까지만 갖도록 함을 지향하고 있어서 이렇게 한 듯.
- 일관적인 함수 사용 방식 : 반환하는 함수와, 반환하지 않는 함수를 섞어 사용 하지 않음.

    ```java
    private void compactExpectedAndActual() { 
    	findCommonPrefix();
    	findCommonSuffix();
    	compactExpected = compactString(expected);
    	compactActual = compactString(actual);
    }
    ```

    ```java
    private void compactExpectedAndActual() { 
    	prefixIndex = findCommonPrefix();
    	suffixIndex = findCommonSuffix();
    	compactExpected = compactString(expected);
    	compactActual = compactString(actual);
    }
    ```

- 함수들 사이의 시간적 결합 제거
    - findCommonPrefix() 가 먼저 호출되고 → findCommonSuffix() 를 호출해야만 하는 상황
        - findCommonSuffix 와 findCommonPrefix 가 따로 존재하기 때문에 누가 순서 달리 해서 호출하면 망함.
    - 첫 시도 : 이런 시간 결합을 외부에 노출 시켜 findCommonPrefix 를 먼저 호출해야함을 드러내기
        - 이를 위해 findCommonSuffix(int prefixIndex) 로만 변경한다면, 사용하지 않는 인수를 받는 형태가 됨
        - 나중에 누가 findCommonSuffix 함수에서 인수를 삭제 해 버리면, 또 다시 prefixIndex 구하는 함수를 먼저 호출해야함에 대해 신경 쓰지 못할 수 있음.
    - 두 번째 시도 : 로직 분리를 위해 findCommonPrefix 는 따로 두지만, findCommonPrefixAndSuffix() 라는 함수 내부에서 **findCommonPrefix 호출 + findCommonSuffix의 로직을 위치** 시킴
        - 순서를 확실하게 할 수 있게 됨.
- index 라는 이름은 사실 Length 여야 할 수도
    - index 는 0 부터 시작하는 애인데, Length 는 그렇지 않을 거임..
    - length 여야 하는 애를 Index 라고 해 두었다면, 코드 곳곳에 +1 하는 코드가 나타나고 있을 확률 높음.
- 불필요한 if 문이 있을 수도 있다
    - 주석 처리하고 실행해서 이전과 같은 결과를 낸다면 제거하자!
- 당연하지만, 함수는 위상적으로 정렬
    - 각 함수는 사용되는 위치 직후에, 위치 시킬 것
- 코드를 리팩토링하다보면, 원래 했던 변경을 되돌리는 경우가 흔하다!!! 시행착오를 반복하는 작업이라는 것 잊지 말기.
- 보이스 카우트 규칙 : 캠핑 한 자리를 내가 도착했던 때 보다, 더 깨끗하게 만들고 떠나자!
