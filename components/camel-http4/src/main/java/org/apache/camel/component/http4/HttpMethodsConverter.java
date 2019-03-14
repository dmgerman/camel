begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|Converter
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * To convert between camel-http4 and camel-http-common for the http methods enums  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|HttpMethodsConverter
specifier|public
specifier|final
class|class
name|HttpMethodsConverter
block|{
DECL|method|HttpMethodsConverter ()
specifier|private
name|HttpMethodsConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toHttp4Methods (org.apache.camel.http.common.HttpMethods method, Exchange exchange)
specifier|public
specifier|static
name|HttpMethods
name|toHttp4Methods
parameter_list|(
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
name|HttpMethods
name|method
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|method
operator|.
name|name
argument_list|()
decl_stmt|;
name|name
operator|=
name|name
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
expr_stmt|;
return|return
name|HttpMethods
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

