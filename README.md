### Realm을 이용한 DB 연습

##### 배운 것

##### 1. Transaction : 수정을 하기 위한 하나의 블럭, 혹시나 수정 중에 에러가 나게 되면 그 Transaction안에 수정들은 다시 원복된다.
```java
realm.executeTransaction(new Realm.Transaction() { //Transaction : 수정을 하기 위한 하나의 블럭, 혹시나 수정 중에 에러가 나게 되면 그 Transaction안에 수정들은 다시 원복된다.
            @Override
            public void execute(Realm realm) {
                result.deleteFirstFromRealm(); //첫번째 검색결과 삭제

                Bbs bbs = result.get(2); // 특정 행 삭제
                bbs.deleteFromRealm();

                result.deleteAllFromRealm(); //검색 결과 전체 삭제
            }
        });
```
##### 또 다른 예제
```java
realm.beginTransaction();
        Number maxValue = realm.where(Bbs.class).max("no");
        int no = (maxValue != null) ? maxValue.intValue()+1 : 1;
        Bbs bbs = realm.createObject(Bbs.class, no); // 레코드 한개 생성
        bbs.setTitle("제목");
        bbs.setContent("내용을 여기");
        bbs.setDate(System.currentTimeMillis());

        // 3. 테이블에 한개의 레코드셋이 들어간다.
        realm.commitTransaction();
```
