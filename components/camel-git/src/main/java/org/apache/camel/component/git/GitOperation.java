begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.git
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|git
package|;
end_package

begin_interface
DECL|interface|GitOperation
specifier|public
interface|interface
name|GitOperation
block|{
DECL|field|CLONE_OPERATION
specifier|public
specifier|final
specifier|static
name|String
name|CLONE_OPERATION
init|=
literal|"clone"
decl_stmt|;
DECL|field|INIT_OPERATION
specifier|public
specifier|final
specifier|static
name|String
name|INIT_OPERATION
init|=
literal|"init"
decl_stmt|;
block|}
end_interface

end_unit

