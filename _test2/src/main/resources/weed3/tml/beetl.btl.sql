#if(id == 1){
    select * from appx where app_id =@{id}
#}else{
   select * from appx where app_id = 10 + @{id}
#}