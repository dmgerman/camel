begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_comment
comment|/**  * Represent the kinds of options when writing a file and what to do in case of an existing file exists.  *  * @version   */
end_comment

begin_enum
DECL|enum|GenericFileExist
specifier|public
enum|enum
name|GenericFileExist
block|{
DECL|enumConstant|Override
DECL|enumConstant|Append
DECL|enumConstant|Fail
DECL|enumConstant|Ignore
DECL|enumConstant|Move
name|Override
block|,
name|Append
block|,
name|Fail
block|,
name|Ignore
block|,
name|Move
block|}
end_enum

end_unit

