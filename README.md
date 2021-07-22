# ToDoアプリ 「ToDo!!」
![todo-sda-ex_main](https://user-images.githubusercontent.com/66711292/126701147-faeb1dac-d640-45e7-8c2a-ff7c2244f18d.png)<br><br>
作成したToDoをリストに登録していく形式で、管理を行うことができるToDoアプリです。<br><br>

ユーザーはToDoに設定した期限や優先度を基にリストのソートを行い、達成すべきToDoから取り掛かることができます。<br>
完了したものや期限切れになったToDoは個別のリストで管理されるため、達成・未達成のログのような使い方ができるほか、
内容を修正して再度達成すべきToDoとして登録し直すこともできます。<br><br>

レスポンシブ対応を行っているため、PCのほかスマートフォン等でもご覧いただけます。<br><br>

## 作成の経緯・目的

## サイトURL
(https://todo-sda-ex.herokuapp.com/)

## 開発環境

## 使用技術

## 各画面説明
### トップページ(ホーム)
![todo-sda-ex_top](https://user-images.githubusercontent.com/66711292/126701202-827cdeb0-ea3b-4f25-a702-ddf23de0d5d7.png)<br><br>
ユーザーが最初にアクセスするページです。<br>
サイトの説明やよくある質問などが記載されています。
<br><br><br>

### ログイン画面
![todo-sda-ex_login](https://user-images.githubusercontent.com/66711292/126701260-bd7152b8-a533-4d50-b7e7-f48d6aca2c75.png)<br><br>
ユーザー登録を行ったIDとパスワードでログインを行います。<br>
お試しログインボタンから、ワンボタンでテストユーザーログインを行うことができます。<br>
ログインユーザーの認証情報はSpring Securityによって管理しています。
<br><br><br>

### ToDo一覧画面(メイン画面)
![todo-sda-ex_todoList](https://user-images.githubusercontent.com/66711292/126712650-f4123112-1cc8-464b-bc60-1a8d939bd734.png)
<br><br>
登録されたToDoがリストで表示されます。<br>
リストは3つのタブで表示することができます。
* 一覧タブ : ユーザーが達成すべきToDoが表示される
* 完了タブ : 完了済みのToDoが表示される
* 期限切れタブ : 設定期限を過ぎているToDoが表示される
<br>
また、期限が当日中のToDoが存在する場合はリスト上方に警告が表示され、選択すると対象のToDoを確認できます。
<br><br>
並び順ドロップダウンからは期限、優先度、更新日時でリストのソートを行うことができます。<br>
リスト右端のアイコンを選択することで、ToDoを完了させる(完了タブの場合は未完了に戻す)ことができます。
<br><br><br>

### ToDo追加画面
![todo-sda-ex_addTodo](https://user-images.githubusercontent.com/66711292/126701286-4021fead-83a3-4b96-a2af-a68273714be6.png)
<br><br><br>

### ToDo詳細画面
![todo-sda-ex_todoDetails](https://user-images.githubusercontent.com/66711292/126701298-d1593de5-2158-4316-833e-d58d311b0069.png)
<br><br><br>

### お問い合わせ画面
![todo-sda-ex_inquiry](https://user-images.githubusercontent.com/66711292/126712937-21e2be0f-cbcb-4f95-bd02-ac7f57360bcb.png)
<br><br><br>

## スマートフォン操作の様子
![todo-sda-ex_demoVideo](https://user-images.githubusercontent.com/66711292/126714216-f874b6b2-ddc6-488f-8338-d95f539c18a6.gif)
