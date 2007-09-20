begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_comment
comment|/**  * Created by IntelliJ IDEA.  * User: gnodet  * Date: Sep 20, 2007  * Time: 11:24:23 AM  * To change this template use File | Settings | File Templates.  */
end_comment

begin_class
DECL|class|CamelNamespaceHandler
specifier|public
class|class
name|CamelNamespaceHandler
extends|extends
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|handler
operator|.
name|CamelNamespaceHandler
block|{
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|registerParser
argument_list|(
literal|"camelContext"
argument_list|,
operator|new
name|CamelContextBeanDefinitionParser
argument_list|(
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createJaxbContext ()
specifier|protected
name|JAXBContext
name|createJaxbContext
parameter_list|()
throws|throws
name|JAXBException
block|{
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.camel.osgi:"
operator|+
name|JAXB_PACKAGES
argument_list|)
return|;
block|}
block|}
end_class

end_unit

