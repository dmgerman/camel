begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * Netty HTTP constants.  */
end_comment

begin_class
DECL|class|NettyHttpConstants
specifier|public
specifier|final
class|class
name|NettyHttpConstants
block|{
DECL|field|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
init|=
literal|"application/x-java-serialized-object"
decl_stmt|;
DECL|field|CONTENT_TYPE_WWW_FORM_URLENCODED
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE_WWW_FORM_URLENCODED
init|=
literal|"application/x-www-form-urlencoded"
decl_stmt|;
DECL|field|HTTP_RESPONSE_TEXT
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_RESPONSE_TEXT
init|=
literal|"CamelHttpResponseText"
decl_stmt|;
DECL|field|HTTP_AUTHENTICATION
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_AUTHENTICATION
init|=
literal|"CamelHttpAuthentication"
decl_stmt|;
DECL|method|NettyHttpConstants ()
specifier|private
name|NettyHttpConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

