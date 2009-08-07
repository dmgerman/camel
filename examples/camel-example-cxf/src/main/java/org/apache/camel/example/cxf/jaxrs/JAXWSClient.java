begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|jaxrs
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
name|example
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
operator|.
name|BookStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
import|;
end_import

begin_class
DECL|class|JAXWSClient
specifier|public
specifier|final
class|class
name|JAXWSClient
block|{
DECL|field|bookStore
specifier|private
name|BookStore
name|bookStore
decl_stmt|;
DECL|method|JAXWSClient ()
specifier|public
name|JAXWSClient
parameter_list|()
block|{
name|JaxWsProxyFactoryBean
name|cfb
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|cfb
operator|.
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|cfb
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9006/soap"
argument_list|)
expr_stmt|;
name|bookStore
operator|=
operator|(
name|BookStore
operator|)
name|cfb
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
DECL|method|getBookStore ()
specifier|public
name|BookStore
name|getBookStore
parameter_list|()
block|{
return|return
name|bookStore
return|;
block|}
block|}
end_class

end_unit

