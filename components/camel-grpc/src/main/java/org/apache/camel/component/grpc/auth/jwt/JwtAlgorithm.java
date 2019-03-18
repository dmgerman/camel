begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.grpc.auth.jwt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|grpc
operator|.
name|auth
operator|.
name|jwt
package|;
end_package

begin_comment
comment|/**  * Implemented JSON Web Token secret signing algorithms  */
end_comment

begin_enum
DECL|enum|JwtAlgorithm
specifier|public
enum|enum
name|JwtAlgorithm
block|{
comment|/**      * HmacSHA256 algorithm      */
DECL|enumConstant|HMAC256
name|HMAC256
block|,
comment|/**      * HmacSHA384 algorithm      */
DECL|enumConstant|HMAC384
name|HMAC384
block|,
comment|/**      * HmacSHA512 algorithm      */
DECL|enumConstant|HMAC512
name|HMAC512
block|}
end_enum

end_unit

