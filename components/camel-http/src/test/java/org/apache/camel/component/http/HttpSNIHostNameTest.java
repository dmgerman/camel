begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|http
operator|.
name|common
operator|.
name|HttpOperationFailedException
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
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
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Ignored test because of external dependency."
argument_list|)
DECL|class|HttpSNIHostNameTest
specifier|public
class|class
name|HttpSNIHostNameTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testMnotDotNetDoesNotReturnStatusCode421 ()
specifier|public
name|void
name|testMnotDotNetDoesNotReturnStatusCode421
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:goodSNI"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMnotDotNetNoSniDoesReturnStatusCode403 ()
specifier|public
name|void
name|testMnotDotNetNoSniDoesReturnStatusCode403
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:noSNI"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
operator|(
name|HttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|is
argument_list|(
literal|403
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMnotDotNetWrongSniDoesReturnStatusCode421 ()
specifier|public
name|void
name|testMnotDotNetWrongSniDoesReturnStatusCode421
parameter_list|()
block|{
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:wrongSNI"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|HttpOperationFailedException
name|cause
init|=
operator|(
name|HttpOperationFailedException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|cause
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|is
argument_list|(
literal|421
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/http/CamelHttpContext.xml"
block|}
argument_list|)
decl_stmt|;
return|return
name|ctx
return|;
block|}
block|}
end_class

end_unit

