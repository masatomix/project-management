jaf-1_1_1.zip
javamail-1_4_1.zip
を取得し、解凍してできる
activation.jar
dsn.jar
imap.jar
mail.jar
mailapi.jar
pop3.jar
smtp.jar
をlibディレクトリに配置してください。

libディレクトリには必要に応じて以下のファイルを配置してください。
・mail.properties
設定していないメールアドレスから投稿が行われて投稿を遮断したときなどに、メールにて管理者等に通知を行うことができます。メール通知を行う場合のメールサーバの情報を記述します。このファイルがなかった場合はメール送信を行いません。

・whitelist.properties
[nu.mine.kino.mail.impl.AddressFilterクラス(http://www.masatom.in/cgi-bin/viewvc.cgi/tags/V1.0.0_20080609_01/MailFilter/source/nu/mine/kino/mail/impl/AddressFilter.java?root=Others&view=log) が参照するメールアドレスのホワイトリストです。ここに書いてあるメールアドレスからの投稿は遮断しません。このファイルがなかった場合は特に遮断処理を行いませんのでご注意ください。

・filter.properties
フィルタ処理が記述されているフィルタの実装クラスを指定します。ここで指定したフィルタが実行されます。ファイルがなかった場合などはなんのフィルタも呼び出されません。デフォルトで送信元をチェックするフィルタが指定されているので、通常は編集の必要はありません。たとえばメーラの種別で投稿をフィルタするとか、独自にフィルタクラスを作成した場合、ココに記述することで独自のフィルタ処理を組み込むことができるようになります。

・log4j.xml
 アプリケーションのデバッグログのための設定ファイルです。