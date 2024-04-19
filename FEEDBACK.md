Places for improvement:
- Decision class is used as model object and better to make model package and put it there.
- DecisionRequest and DecisionResponse are used as request and response objects and better to make payload package and put them there.
- exceptions package is not appropriate name better to use singular form exception to keep project structure consistent.
- DecisionEngineConstants class is better to rename to singular form DecisionEngineConstant to keep project structure consistent.
- I would highlight that is better to use extends RuntimeException instead of extends Throwable in custom exceptions ( if you will use in future exception handling by Spring Boot options) if you're planning to use unchecked exceptions or just Exception to use checked exceptions.
- I would suggest to annotate Response classes with @NoArgsConstructor almost always because @RequestBody annotation in controller methods requires default constructor (empty).

Good things:
- Good usage of Lombok annotations.
- Understandable and readable methods naming!
- Well-structured project.
- Tests are well written and cover main cases.
- Methods are well documented.

I have noticed 2 main shortcomings in the code. First is how handled exceptions in the code and second is how service methods are implemented (overall design). In requirements are mentioned that project should follow
solid principles as much as possible. I found that Single Responsibility Principle is not followed in the code. Service methods are doing more than one thing. For example, in the DecisionEngine class, the methods
are doing (creditModifier calculation, input validation, and decision (loan amount calculation)). I decided to separate these responsibilities into different classes. I created a new classes such 
as CreditModifierCalculator and LoanInputValidator to do so. I decided not to do additional class for period calculation because it requires method highestValidLoanAmount(int loanPeriod) 
which is used also in valid loan calculation, so I decided to keep period and amount in the same class because it can be considered as one responsibility.  


Second strong point to improve is exception handling. I decided to implement GlobalExceptionHandler controller class to handle exceptions in proper way than it was done before.
I change custom exceptions classes to extend one main exception class "ExceptionTemplate" which is used as abstract class for all custom exceptions. So it could be used in GlobalExceptionHandler methods as argument
to pass custom exceptions that extends ExceptionTemplate. it makes controllers cleaner and more readable.

Also I have found one issue if input would be incorrect then the response status would be 200 OK instead of 400 Bad Request. I decided to change it by removing try catch
block in DecisionEngine class and let GlobalExceptionHandler handle it. It will return 400 Bad Request status if input is incorrect.
```