begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
package|;
end_package

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FileLanguage
specifier|public
class|class
name|FileLanguage
extends|extends
name|SimpleLanguage
block|{
DECL|method|FileLanguage ()
specifier|public
name|FileLanguage
parameter_list|()
block|{
comment|// do not allow escaping, as Windows uses \ as path separator
name|allowEscape
operator|=
literal|false
expr_stmt|;
block|}
block|}
end_class

end_unit

