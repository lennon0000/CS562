with v1 as (
    select cust, month, avg(quant) as avg_q
    from sales
    where year = 1997
    group by cust, month),

v2 as (
    select s.cust, s.month, avg(t.quant) as avg_prev
    from sales s, sales t
    where s.year = 1997 and t.year = 1997 and t.cust = s.cust and t.month < s.month
    group by s.cust, s.month),
    
v3 as (
    select s.cust, s.month, avg(t.quant) as avg_after
    from sales s, sales t
    where s.year = 1997 and t.year = 1997 and t.cust = s.cust and t.month > s.month
    group by s.cust, s.month)
    
select v1.cust, v1.month, v1.avg_q, v2.avg_prev, v3.avg_after
from v1 full outer join v2 
on v1.cust = v2.cust and v1.month = v2.month
full outer join v3
on v1.cust = v3.cust and v1.month = v3.month
order by v1.cust, v1.month;