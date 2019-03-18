begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|// CHECKSTYLE:OFF
end_comment

begin_comment
comment|// we want to use the code as-is as that is how end users may code
end_comment

begin_class
DECL|class|Constants
specifier|public
class|class
name|Constants
block|{
DECL|field|BAR
specifier|public
specifier|static
name|String
name|BAR
init|=
literal|"456"
decl_stmt|;
DECL|class|MyInnerStuff
specifier|public
specifier|static
class|class
name|MyInnerStuff
block|{
DECL|field|FOO
specifier|public
specifier|static
name|String
name|FOO
init|=
literal|"123"
decl_stmt|;
block|}
block|}
end_class

begin_comment
comment|// CHECKSTYLE:ON
end_comment

end_unit

