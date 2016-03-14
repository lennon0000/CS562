drop view v1;
create view v1 as (
    select cust, state, avg(quant) as avg_q
    from sales
    where year = 1997
    group by cust, state);

select x.cust, x.avg_q, y.avg_q, z.avg_q
from v1 x, v1 y, v1 z
where x.cust = y.cust and x.cust = z.cust and x.state = 'NY' and y.state = 'CT'
and z.state = 'NJ' and x.avg_q > y.avg_q and x.avg_q > z.avg_q;