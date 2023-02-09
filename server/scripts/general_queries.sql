-- Test your queries here before adding it into the code
select * from jobsearch.appuser;
select * from jobsearch.tag;

insert into tag(default_name, tag_code, created_by) values('Ruby', 'E200', 'test@test.com');
update jobsearch.tag set default_name = 'C#';
update jobsearch.tag set default_name = 'C#' where tag_id = 1;

drop trigger tag_updated_at_automatic on jobsearch.tag;
drop function update_value_updated_at;
