begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.htmlunit.pages
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|htmlunit
operator|.
name|pages
package|;
end_package

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|WebDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|WebElement
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|By
operator|.
name|name
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|openqa
operator|.
name|selenium
operator|.
name|By
operator|.
name|xpath
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|EndpointsPage
specifier|public
class|class
name|EndpointsPage
block|{
DECL|field|webDriver
specifier|private
specifier|final
name|WebDriver
name|webDriver
decl_stmt|;
DECL|method|EndpointsPage (WebDriver webDriver)
specifier|public
name|EndpointsPage
parameter_list|(
name|WebDriver
name|webDriver
parameter_list|)
block|{
name|this
operator|.
name|webDriver
operator|=
name|webDriver
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri)
specifier|public
name|void
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|WebElement
name|form
init|=
name|getCreateEndpointForm
argument_list|()
decl_stmt|;
name|form
operator|.
name|findElement
argument_list|(
name|name
argument_list|(
literal|"uri"
argument_list|)
argument_list|)
operator|.
name|sendKeys
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|form
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
DECL|method|getCreateEndpointForm ()
specifier|public
name|WebElement
name|getCreateEndpointForm
parameter_list|()
block|{
return|return
name|webDriver
operator|.
name|findElement
argument_list|(
name|xpath
argument_list|(
literal|"//form[@name = 'createEndpoint']"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

