begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Produce
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|SpringWebserviceProducer
operator|.
name|CamelHttpUrlConnectionMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|SpringWebserviceProducer
operator|.
name|CamelHttpsUrlConnectionMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpUrlConnectionMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpsUrlConnectionMessageSender
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
name|assertFalse
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
name|assertSame
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
name|assertTrue
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
name|fail
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ProducerRemoteRouteTimeOutTest
specifier|public
class|class
name|ProducerRemoteRouteTimeOutTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|xmlRequestForGoogleStockQuote
specifier|private
specifier|final
name|String
name|xmlRequestForGoogleStockQuote
init|=
literal|"<GetQuote xmlns=\"http://www.webserviceX.NET/\"><symbol>GOOG</symbol></GetQuote>"
decl_stmt|;
annotation|@
name|Produce
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Ignore
argument_list|(
literal|"Run manually, makes connection to external webservice"
argument_list|)
annotation|@
name|Test
DECL|method|callStockQuoteWebserviceCommonsHttpWith3MillSecondsTimeout ()
specifier|public
name|void
name|callStockQuoteWebserviceCommonsHttpWith3MillSecondsTimeout
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceCommonsHttpWith3MillSecondsTimeout"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Miss the expected exception in chain"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|cee
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|hasThrowableInChain
argument_list|(
name|cee
argument_list|,
name|SocketTimeoutException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Run manually, makes connection to external webservice"
argument_list|)
annotation|@
name|Test
DECL|method|callStockQuoteWebserviceCommonsHttpWith5000MillSecondsTimeout ()
specifier|public
name|void
name|callStockQuoteWebserviceCommonsHttpWith5000MillSecondsTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceCommonsHttpWith5000MillSecondsTimeout"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Run manually, makes connection to external webservice"
argument_list|)
annotation|@
name|Test
DECL|method|callStockQuoteWebserviceJDKWith3MillSecondsTimeout ()
specifier|public
name|void
name|callStockQuoteWebserviceJDKWith3MillSecondsTimeout
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceJDKWith3MillSecondsTimeout"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Miss the expected exception in chain"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|cee
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|hasThrowableInChain
argument_list|(
name|cee
argument_list|,
name|SocketTimeoutException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Ignore
argument_list|(
literal|"Run manually, makes connection to external webservice"
argument_list|)
annotation|@
name|Test
DECL|method|callStockQuoteWebserviceJDKWith5000MillSecondsTimeout ()
specifier|public
name|void
name|callStockQuoteWebserviceJDKWith5000MillSecondsTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:stockQuoteWebserviceJDKWith5000MillSecondsTimeout"
argument_list|,
name|xmlRequestForGoogleStockQuote
argument_list|)
expr_stmt|;
block|}
DECL|method|hasThrowableInChain (Throwable throwable, Class<? extends Throwable> clazz)
specifier|private
specifier|static
name|boolean
name|hasThrowableInChain
parameter_list|(
name|Throwable
name|throwable
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|.
name|isAssignableFrom
argument_list|(
name|throwable
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|hasThrowableInChain
argument_list|(
name|throwable
operator|.
name|getCause
argument_list|()
argument_list|,
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|verifyTheFieldPopulationFromHttpUrlConnectionMessageSenderToCamelHttpUrlConnectionMessageSender ()
specifier|public
name|void
name|verifyTheFieldPopulationFromHttpUrlConnectionMessageSenderToCamelHttpUrlConnectionMessageSender
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpUrlConnectionMessageSender
name|fromConnectionMessageSender
init|=
operator|new
name|HttpUrlConnectionMessageSender
argument_list|()
decl_stmt|;
name|fromConnectionMessageSender
operator|.
name|setAcceptGzipEncoding
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CamelHttpUrlConnectionMessageSender
name|toConnectionMessageSender
init|=
operator|new
name|CamelHttpUrlConnectionMessageSender
argument_list|(
operator|new
name|SpringWebserviceConfiguration
argument_list|()
argument_list|,
name|fromConnectionMessageSender
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"acceptGzipEncoding property didn't get populated"
argument_list|,
name|toConnectionMessageSender
operator|.
name|isAcceptGzipEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|fromConnectionMessageSender
operator|.
name|setAcceptGzipEncoding
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|toConnectionMessageSender
operator|=
operator|new
name|CamelHttpUrlConnectionMessageSender
argument_list|(
operator|new
name|SpringWebserviceConfiguration
argument_list|()
argument_list|,
name|fromConnectionMessageSender
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"acceptGzipEncoding property didn't get populated properly!"
argument_list|,
name|toConnectionMessageSender
operator|.
name|isAcceptGzipEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|verifyTheFieldPopulationFromHttpsUrlConnectionMessageSenderToCamelHttpsUrlConnectionMessageSender ()
specifier|public
name|void
name|verifyTheFieldPopulationFromHttpsUrlConnectionMessageSenderToCamelHttpsUrlConnectionMessageSender
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpsUrlConnectionMessageSender
name|fromConnectionMessageSender
init|=
operator|new
name|HttpsUrlConnectionMessageSender
argument_list|()
decl_stmt|;
name|fromConnectionMessageSender
operator|.
name|setAcceptGzipEncoding
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fromConnectionMessageSender
operator|.
name|setHostnameVerifier
argument_list|(
operator|new
name|HostnameVerifier
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|s
parameter_list|,
name|SSLSession
name|sslsession
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fromConnectionMessageSender
operator|.
name|setKeyManagers
argument_list|(
operator|new
name|KeyManager
index|[]
block|{
operator|new
name|KeyManager
argument_list|()
block|{
block|}
block|}
block|)
class|;
end_class

begin_expr_stmt
name|fromConnectionMessageSender
operator|.
name|setSecureRandom
argument_list|(
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|fromConnectionMessageSender
operator|.
name|setSslProtocol
argument_list|(
literal|"sslProtocol"
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|fromConnectionMessageSender
operator|.
name|setSslProvider
argument_list|(
literal|"sslProvider"
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|fromConnectionMessageSender
operator|.
name|setTrustManagers
argument_list|(
operator|new
name|TrustManager
index|[]
block|{
operator|new
name|TrustManager
argument_list|()
block|{
block|}
end_expr_stmt

begin_empty_stmt
unit|})
empty_stmt|;
end_empty_stmt

begin_decl_stmt
name|CamelHttpsUrlConnectionMessageSender
name|toConnectionMessageSender
init|=
operator|new
name|CamelHttpsUrlConnectionMessageSender
argument_list|(
operator|new
name|SpringWebserviceConfiguration
argument_list|()
argument_list|,
name|fromConnectionMessageSender
argument_list|)
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|assertFalse
argument_list|(
literal|"acceptGzipEncoding field didn't get populated"
argument_list|,
name|toConnectionMessageSender
operator|.
name|isAcceptGzipEncoding
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_for
for|for
control|(
name|Field
name|expectedField
range|:
name|fromConnectionMessageSender
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|expectedField
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|expectedField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|fieldName
init|=
name|expectedField
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Field
name|actualField
init|=
name|toConnectionMessageSender
operator|.
name|getClass
argument_list|()
operator|.
name|getSuperclass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
name|actualField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"The field '"
operator|+
name|fieldName
operator|+
literal|"' didn't get populated properly!"
argument_list|,
name|expectedField
operator|.
name|get
argument_list|(
name|fromConnectionMessageSender
argument_list|)
argument_list|,
name|actualField
operator|.
name|get
argument_list|(
name|toConnectionMessageSender
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_for

unit|}  }
end_unit

