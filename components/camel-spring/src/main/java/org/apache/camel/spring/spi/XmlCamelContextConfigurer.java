begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
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
name|spring
operator|.
name|SpringCamelContext
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
name|ApplicationContext
import|;
end_import

begin_comment
comment|/**  * Allows to do custom configuration when a new XML based {@link org.apache.camel.spring.SpringCamelContext} has  * been created. For example we use this to enable camel-spring-boot to configure Camel created  * from XML files with the existing Spring Boot auto configuration.  */
end_comment

begin_interface
DECL|interface|XmlCamelContextConfigurer
specifier|public
interface|interface
name|XmlCamelContextConfigurer
block|{
comment|/**      * Configures XML based CamelContext with the given configuration      *      * @param applicationContext the Spring context      * @param camelContext       the XML based CamelContext      * @throws Exception is thrown if error during configuration      */
DECL|method|configure (ApplicationContext applicationContext, SpringCamelContext camelContext)
name|void
name|configure
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|SpringCamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

