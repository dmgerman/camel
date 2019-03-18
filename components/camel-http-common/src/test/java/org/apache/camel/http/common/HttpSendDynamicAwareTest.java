begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|SendDynamicAware
operator|.
name|DynamicAwareEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|HttpSendDynamicAwareTest
specifier|public
class|class
name|HttpSendDynamicAwareTest
block|{
DECL|field|httpSendDynamicAware
specifier|private
name|HttpSendDynamicAware
name|httpSendDynamicAware
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|=
operator|new
name|HttpSendDynamicAware
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpUndefinedPortWithPathParseUri ()
specifier|public
name|void
name|testHttpUndefinedPortWithPathParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"http://localhost/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not add port if http and not specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpsUndefinedPortParseUri ()
specifier|public
name|void
name|testHttpsUndefinedPortParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"https"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"https://localhost/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not add port if https and not specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttp4UndefinedPortWithPathParseUri ()
specifier|public
name|void
name|testHttp4UndefinedPortWithPathParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"http4"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"http4://localhost/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not add port if http4 and not specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttps4UndefinedPortParseUri ()
specifier|public
name|void
name|testHttps4UndefinedPortParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"https4"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"https4://localhost/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not add port if https4 and not specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpPort80ParseUri ()
specifier|public
name|void
name|testHttpPort80ParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"http://localhost:80/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not port if http and port 80 specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpsPort443ParseUri ()
specifier|public
name|void
name|testHttpsPort443ParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"https"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"https://localhost:443/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should not port if https and port 443 specified"
argument_list|,
literal|"localhost"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpPort8080ParseUri ()
specifier|public
name|void
name|testHttpPort8080ParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"http://localhost:8080/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should add port if http and port other than 80 specified"
argument_list|,
literal|"localhost:8080"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpsPort8443ParseUri ()
specifier|public
name|void
name|testHttpsPort8443ParseUri
parameter_list|()
block|{
name|this
operator|.
name|httpSendDynamicAware
operator|.
name|setScheme
argument_list|(
literal|"https"
argument_list|)
expr_stmt|;
name|DynamicAwareEntry
name|entry
init|=
operator|new
name|DynamicAwareEntry
argument_list|(
literal|"https://localhost:8443/test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
index|[]
name|result
init|=
name|httpSendDynamicAware
operator|.
name|parseUri
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Parse should add port if https and port other than 443 specified"
argument_list|,
literal|"localhost:8443"
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

