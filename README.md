# [ 들어가기 전, 틀 세팅 및 구성하기 ]

## 1. 프로젝트 생성 및 의존성 설정

1.   프로젝트 생성 : Java 21
2. 의존성 설정
    - lombok
    - spring boot devtools
    - spring web
    - spring data JPA
    - MYSQL Driver
    - Mustache

## 2. 야물 설정_MYSQL 연결

## 3. 테이블 생성

1. 구매자테이블(buyer_tb)
    1. Buyer
    2. Buyercontroller
    3. Buyerrepository
    4. BuyerRequest
    5. BuyerService

1. 상품테이블(product_tb)
    1. Product
    2. ProductController
    3. ProductRepository
    4. ProductResponse
    5. ProductService

1. 구매테이블(purchase_tb)
    1. Purchase
    2. PurchaseController
    3. PurchaseRepository
    4. PurchaseRequest
    5. PurchaseResponse
    6. PurchaseService

![1](https://github.com/thdus1323/team-5_buyermarket_buyer/assets/153582422/a22bbf4a-9393-4268-8698-97fca4cc9798)


# [ 기능 ]

## 구매자 회원가입

join-form.mustache [뷰] : action, 메서드, 기본 폼, 내용 확인

```java
 **<form action="/join" method="post" enctype="application/x-www-form-urlencoded">**
                <div class="mb-3">
                    <input id="buyerName" type="text" class="form-control" placeholder="Enter buyerName"
                           name="buyerName" value="ssar">
                </div>
                <div class="mb-3">
                    <input type="buyerPw" class="form-control" placeholder="Enter password" name="buyerPw" value="1234">
                </div>
                <div class="mb-3">
                    <input type="buyerEmail" class="form-control" placeholder="Enter email" name="buyerEmail" value="ssar@nate.com">
                </div>
                <button type="submit" class="btn btn-primary form-control">회원가입</button>
            </form>
```

BuyerController : 회원가입 뷰로 가능 기능, reqDTO를 통해 join 정보 가져오기

```java
//회원가입
    @PostMapping("/join")
    public String join(BuyerRequest.JoinDTO reqDTO) {
        buyerService.joinByNameAndPwAndEmail(reqDTO);
        System.out.println("회원가입 : " + reqDTO);
        return "redirect:/login-form"; // 나중에 login-form으로
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "buyer/join-form";
    }

```

BuyerService : join을 위해 필요한 정보(이름, 패스워드, 이메일)들을 Repository로 전달

```java
 //회원가입
    @Transactional
    public void joinByNameAndPwAndEmail(BuyerRequest.JoinDTO reqDTO){
        buyerRepository.join(reqDTO);
    }
```

BuyerRepository : db에 입력되는 쿼리 입력

```java
 //회원가입
    public void join(BuyerRequest.JoinDTO reqDTO) {
        Query query = em.createNativeQuery(
                """
                        insert into buyer_tb(buyer_name, buyer_pw, buyer_email, created_at) values(?,?,?,now())
                        """);
        query.setParameter(1, reqDTO.getBuyerName());
        query.setParameter(2, reqDTO.getBuyerPw());
        query.setParameter(3, reqDTO.getBuyerEmail());
        query.executeUpdate();
    }
```

## 구매자 로그인/로그아웃

1) 로그인

(1) BuyerController : 로그인 뷰 기능, 로그인 정보를 세션에 담아 DTO를 통해 요

```java
@PostMapping("login")
    public String login(BuyerRequest.LoginDTO reqDTO) {
        Buyer sessionBuyer = buyerService.loginByNameAndPw(reqDTO);
        session.setAttribute("sessionBuyer", sessionBuyer);
        return "redirect:/";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "buyer/login-form";
    }

```

(2) BuyerService : 로그인에 필요한 이름, 패스워드를 요청 디티오에 담아서 세션에 담아줌.

```java
//로그인
    public Buyer loginByNameAndPw(BuyerRequest.LoginDTO reqDTO){
        Buyer sessionBuyer = buyerRepository.login(reqDTO);
        return sessionBuyer;
    }
```

(3) BuyerRepository : 로그인에 필요한 정보(이름, 패스워드)를 DTO에 담아 요청하고 그것을 db에서 가져옴.

```java
 //로그인
    public Buyer login(BuyerRequest.LoginDTO reqDTO) {
        Query query = em.createNativeQuery("select * from buyer_tb where buyer_name=? and buyer_pw=?", Buyer.class);
        query.setParameter(1, reqDTO.getBuyerName());
        query.setParameter(2, reqDTO.getBuyerPw());
        Buyer sessionBuyer = (Buyer) query.getSingleResult();
        return sessionBuyer;
    }

```

2) 로그아웃

(1) BuyerController : 로그아웃하면 세션 삭제?

```java
 @GetMapping("/logout")
    public String logout()
    { session.invalidate();
        return "redirect:/";}
```

## 상품 목록보기

1) ProductController : 문자열 리스트로 반환하는 메서드에 목록 요청이 들어오면, list 목록이 응답 된다.

```java
 @GetMapping({"/product","/"})
    public String list(HttpServletRequest request){
        List<ProductResponse.ListDTO> productList = productService.getProductList();
        request.setAttribute("productList", productList);
        return "product/list";
    }
```

2) ProductService :  요청한 해당 목록을 가져오는 건데, 레파지토리에서 해당 목록을 list 타입으로 다 가져온다. 그런데 db와 java는 다른 언어로, 데이터의 형식이 다를 수 있어 db의 정보를 바로 java에서 쓸 수 없다. 그래서 db에서 가져온 정보들을 바로 가져가지 않고 스트림에 뿌려서 알기 쉽게 가공을 하고 다시 리스트에 담아서 그것을 응답해줌.

```java
 public List<ProductResponse.ListDTO> getProductList(){
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(ProductResponse.ListDTO::new)
                .collect(Collectors.toList());
    }
```

3) ProductRepository : db에서 상품목록을 가져오는 쿼리 작성.

그 리스트는 1개가 아니니까 getResultList로 반환받는다.

```java
public List<Product> findAll() {
        Query query = em.createNativeQuery("select * from product_tb order by product_id desc", Product.class);
        return query.getResultList();
    }
```

## 상품 상세보기

1)ProductController : 상품아이디에 해당하는 정보들을 얻고 싶을 때 그에 해당하는 요청을 하면, 서비스에서 해당 상품 디테일을 받아오는데 그것을 detail dto에 담는다.

```java
 @GetMapping("/product/{productId}/detail")
    public String detail(@PathVariable Integer productId, HttpServletRequest request){
        ProductResponse.DetailDTO product = productService.getProductDetail(productId);
        request.setAttribute("product",product);
        return "product/detail";
    }
```

2) ProductService : 상품아이디로 상세보기를 dto에 담아올거다. 레파지토리에서 정보를 가져올 거다. 그래서 새로운 detaildto로 응답을 받을 거다.

```java
   public ProductResponse.DetailDTO getProductDetail(Integer productId){
        Product product = productRepository.findById(productId);
        return new ProductResponse.DetailDTO(product);
    }
```

3) ProductRepository : db에서 상품 아이디에 대한 정보를 가져올거다.

```java
 public Product findById(int productId){
        Query query = em.createNativeQuery("select * from product_tb where product_id=?", Product.class);
        query.setParameter(1,productId);
        return (Product) query.getSingleResult();
    }
```

## 구매가 되면, 상품의 재고 수정이 필요함(qty가 줄어야 함.)

1) PurchaseController : 상품id pk와 수정요청을 한다. 서비스에 해당로직을 신청.메인이 반환된다.

```java
 @PostMapping("/purchase/{purId}/update")
    public String update(@PathVariable Integer purId, PurchaseRequest.UpdateDTO reqDTO) {
        purchaseService.changePurQty(purId, reqDTO);
        return "redirect:/";
    }
```

2) PurchaseService : 구매자pk와 업데이트 디티오를 요청을 하고, 레파지토리에 요청을 한다.-v

```java
   @Transactional
    public void changePurQty(Integer buyerId, PurchaseRequest.UpdateDTO reqDTO){
        purchaseRepository.updateById(buyerId, reqDTO);
    }
```

3)PurchaseRepository : db에 업데이트 요청 쿼리 작성

```java
 public void updateQty(PurchaseRequest.SaveDTO reqDTO){
        String q = """
                update product_tb set product_qty = product_qty - ? where product_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2, reqDTO.getProductId());
        query.executeUpdate();
    }
```

## 구매가 되면, order 테이블에 구매 목록이 insert 되어야 함 & 구매 목록 보기 기능이 필요함.

1) 나의 구매 목록 보기

(1) PurchaseController : 세션에 있는 buyer가 요청을 하면, 그 사람 구매목록만 나오게 함

-session을 활용하여 그 사람인지 인식하고 해당 사람이 산 구매리스트(1사람이 여러 물품을 살 수 있다.)를 반환하도록 한다.

```java
 @GetMapping("/purchase/list")
    public String list(HttpServletRequest request, HttpSession session) {
        //list에는 구매한 사람만 나오게
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");
        List<Purchase> purchaseList = purchaseService.getPurchaseList(sessionBuyer.getBuyerId());
        request.setAttribute("purchaseList", purchaseList);
        return "purchase/list";
    }
```

(2) PurchaseService : 로그인을 먼저 했기 때문에 레파지토리에 해당 buyerid를 찾아보면 된다. 해당 buyterId를 요청해보고 그 정보가 있는지 확인해야 한다. 그리고 있다면 그 사람의 purchaselist를 반환하도록 한다.

```java
 public List<Purchase> getPurchaseList(Integer buyerId){
        List<Purchase> purchaseList = purchaseRepository.findByBuyerId(buyerId);
        return purchaseList;
    }
```

(3) PurchaseRepository : buyerid의 구매목록(리스트)을 조회하는 쿼리를 짠다.

```java
public List<Purchase> findByBuyerId(Integer buyerId) {
        String q = """
                select * from purchase_tb where buyer_id = ?
                """;
        Query query = em.createNativeQuery(q, Purchase.class);
        query.setParameter(1, buyerId);
        List<Purchase> purchaseList = query.getResultList();
        return purchaseList;
    }
```

2) 나의 구매목록에 새로운 정보 저장 : 새로 구입하는 물품들이 있을 수 있기에 해당 경우,그것들을 db에 저장하는 쿼리를 짠다.

```java
public void save(Integer buyerId, String buyerName,  PurchaseRequest.SaveDTO reqDTO) {
        String q = """
                insert into purchase_tb(buyer_id, buyer_name, product_id, product_name, product_price, product_qty, pur_qty, created_at) 
                values (?, ?, ?, ?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
            query.setParameter(1, buyerId);
            query.setParameter(2, buyerName);
            query.setParameter(3, reqDTO.getProductId());
            query.setParameter(4, reqDTO.getProductName());
            query.setParameter(5, reqDTO.getProductPrice());
            query.setParameter(6, reqDTO.getProductQty());
            query.setParameter(7, reqDTO.getPurQty());

            query.executeUpdate();
    }
```

3) 내 구매에 따른 상품재고 수량 변경 : 구매수량에 따른 상품재고는 차감이 되어야 하기에 해당 뺄셈 쿼리를 짠다.

```java
public void updateQty(PurchaseRequest.SaveDTO reqDTO){
        String q = """
                update product_tb set product_qty = product_qty - ? where product_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2, reqDTO.getProductId());
        query.executeUpdate();
    }
```

## 그 밖의 기능들

-구매수량 수정 : 구매수량을 변경하는 로직.

구매 id(pk)와 구매요청을 하면 문자열로 업데이트가 된다. 

자세하게는 서비스에 수량변경요청이 가고 요청 후에는 메인으로 반환 된다.

```java
@PostMapping("/purchase/{purId}/update")
    public String update(@PathVariable Integer purId, PurchaseRequest.UpdateDTO reqDTO) {
        purchaseService.changePurQty(purId, reqDTO);
        return "redirect:/";
    }
```

-상품 구매 결정 : 상품 구매와 함께 로그인한 정보로 구매 정보가 넘어가는 것이 핵심.

구매 요청을 buyersession에 저장되어 있는 id, name에 저장.

그래서 해당 buyerid의 구매만 나오게 하는 것이 핵심.

```java
@PostMapping("/purchase/save")
    public String saveByBuyerName(PurchaseRequest.SaveDTO reqDTO) {
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");

        System.out.println("상품 구매하기 디티오 : " + reqDTO);
        System.out.println("출력되는지 확인 중입니다. 세션유저 : " + sessionBuyer.getBuyerId() + sessionBuyer.getBuyerName());

        purchaseService.savePurchase(sessionBuyer.getBuyerId(), reqDTO);

        return "redirect:/purchase/list";
    }
```

⭐DB와 자바 연결이 중요.

**-product 클래스의 필드 값들이 DB에서는 tb의 열이 된다.**

난이도 상🔝) sessionBuyerId의 구매 목록을 조회하는 것이 핵심.

-로그인 했던 id의 session을 이용하여 식별 및 관련 내용 추출 + 구매목록 조회
