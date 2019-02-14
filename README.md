## xplatform-utility
xplatform 관련 유틸리티 

### enable_acc_label.py 
Xplatform에서 Accessibility를 enable 하고, 각 컴포넌트에 label을 설정하게 되면 UiStudio 에서 Element를 쉽게 식별할 수 있습니다. 
컴포넌트 지정시 사용한 id를 lable로 매팅시켜주는 python 코드를 실행하시면 자동으로 id 값을 Element의 label로 바꿔줍니다. 
```
python --file test.xfdl  --out test.xfdl  
```
--out 옵션을 주지 않으면 파일은 "updated_"가 prefix로 붙어지면서 만들어지게 됩니다. 

### TobeSoft_Table.xaml 
Xplatform에서 DataScraping이 동작하지 않는 경우 Xplatform이 table을 표현하는 방식을 이해해서 DataTable로 데이터를 추출할수 있는 샘플 workflow 입니다. role='table'인 항목에 대한 Selector는 해당 xplatform 환경에 맞게 변경해주시면 됩니다. 
