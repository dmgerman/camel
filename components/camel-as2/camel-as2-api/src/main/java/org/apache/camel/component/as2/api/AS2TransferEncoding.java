begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
package|;
end_package

begin_interface
DECL|interface|AS2TransferEncoding
specifier|public
interface|interface
name|AS2TransferEncoding
block|{
DECL|field|NONE
name|String
name|NONE
init|=
literal|null
decl_stmt|;
DECL|field|BASE64
name|String
name|BASE64
init|=
literal|"base64"
decl_stmt|;
DECL|field|SEVENBIT
name|String
name|SEVENBIT
init|=
literal|"7bit"
decl_stmt|;
block|}
end_interface

end_unit

