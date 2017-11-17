package com.project.realmprac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.project.realmprac.realm.Bbs;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create();
        read();
    }

    public void create(){
        /* 동기로 데이터 입력(한개의 스레드에서 순서대로 작업 처리) */
        // 1. 인스턴스 생성
        Realm realm = Realm.getDefaultInstance();

        // 2. 트랜잭션 시작
        realm.beginTransaction();
        Number maxValue = realm.where(Bbs.class).max("no");
        int no = (maxValue != null) ? maxValue.intValue()+1 : 1;
        Bbs bbs = realm.createObject(Bbs.class, no); // 레코드 한개 생성
        bbs.setTitle("제목");
        bbs.setContent("내용을 여기");
        bbs.setDate(System.currentTimeMillis());

        // 3. 테이블에 한개의 레코드셋이 들어간다.
        realm.commitTransaction();

        /* 비동기로 데이터 입력(여러개의 스레드에서 병렬적으로 작업 처리) */
        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm asyncRealm) {
                Bbs bbs = asyncRealm.createObject(Bbs.class, no);
                bbs.setTitle("제목");
                bbs.setContent("내용을 여기");
                bbs.setDate(System.currentTimeMillis());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //데이터 베이스 처리가 끝나고 호출될 함수를 정의할 수 있음
            }
        });
    }

    public void read(){
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Bbs> query = realm.where(Bbs.class);
        query.equalTo("no", 1);
        query.or();
        query.equalTo("title", "제목");

        // 또는 query.or().equalTo() 도 사용 가능

        // 동기로 find
        RealmResults<Bbs> result = query.findAll();
        // 비동기로 find
        RealmResults<Bbs> result2 = query.findAllAsync();

        result2.addChangeListener(new RealmChangeListener<RealmResults<Bbs>>() {
            @Override
            public void onChange(RealmResults<Bbs> bbs) {

            }
        });

        Log.d("Result", "쿼리결과 = " + result2.toString());

    }

    public void update(){
        // 1. 수정할 객체 가져오거나 새엇ㅇ
        Bbs bbs = new Bbs();
        //bbs.setNo(99); //primary 값이 99번인 데이터를 찾고, 있으면 수정이 됨, 없으면 insert 됨
        bbs.setTitle("수정할 제목");
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(bbs);
            }
        });

    }

    public void delete(){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Bbs> result = realm.where(Bbs.class).findAll();

        realm.executeTransaction(new Realm.Transaction() { //Transaction : 수정을 하기 위한 하나의 블럭, 혹시나 수정 중에 에러가 나게 되면 그 Transaction안에 수정들은 다시 원복된다.
            @Override
            public void execute(Realm realm) {
                result.deleteFirstFromRealm(); //첫번째 검색결과 삭제

                Bbs bbs = result.get(2); // 특정 행 삭제
                bbs.deleteFromRealm();

                result.deleteAllFromRealm(); //검색 결과 전체 삭제
            }
        });

    }
}
