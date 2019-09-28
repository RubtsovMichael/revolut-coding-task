# revolut-coding-task

Assumptions

Very simple Account object contains only Number and Amount, no currencies and exchange rates.<br>
No way to create accounts through API.<br>
There is small population of accounts for demo purposes in InitialAccounts.

REST end points:<br>
GET /accounts - convenience endpoint to list existing accounts numbers<br>
GET /accounts/{number} - get account by number, returns json with amount and number like<br> 
    {
        "number":"2222222233334444",
        "amount":12.56
    }
    <br>
POST /makeTransfer - makes transfer, consumes body with TransferOrder JSON, example<br>
    {
        "from" : "5555222233334444",
        "to" : "2222222233334444",
        "amount" : 12.56
    }
    
REST implemented in AccountsController with usage of Jersey web framework.<br>
Application entry point is defined in App.java, which starts Grizzly container
with controller, logic implementation and accounts population.<br>
Application is started at http://localhost:8080/api/, so URLs look like http://localhost:8080/api/accounts/2222222233334444

Transfers synchronization achieved by simultaneous lock of both accounts (reentrant locks used), see BlockingTransferExecutor.<br>
Deadlocks prevented by locking accounts in ascending order. Lock attempt times out in 3 sec. 

Application build is controlled by maven.<br>
Unit testing implemented in Jupiter (Junit 5).<br>
Logic is covered by unit tests.<br>
REST is covered with e2e tests implemented with Jupiter and rest-assured in AccountsControllerTest.
This test starts container, executes several REST invocations and stops it.<br>
Application bundle is built by maven assembly plugin, bundle can be run with<br>
java -jar target/coding-task-1.0-SNAPSHOT-jar-with-dependencies.jar

To build and run application execute run.bat, which does maven package with tests and starts application.