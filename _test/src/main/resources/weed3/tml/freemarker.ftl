<#if id == 1>
    select * from appx where app_id =${id}
<#else>
    select ${id} as app_id
</#if>