
## Technical Queries

### Doesn't reactive hibernate use JDBC that blocks?
No. Reactive hibernate uses the vertx driver and does not block.
https://hibernate.org/reactive/documentation/1.0/reference/html_single/


### Why use reactive hibernate over R2DBC?
Reactive hibernate 
- is non blocking
- avoids embedding the SQL queries and relations
  - reduces code maintenance
  - the crud work is very simple and can be handled by hibernate

Mutiny is used to interface with reactor.