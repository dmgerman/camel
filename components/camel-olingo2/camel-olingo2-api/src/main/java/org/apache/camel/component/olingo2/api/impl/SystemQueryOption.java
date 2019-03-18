begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
operator|.
name|api
operator|.
name|impl
package|;
end_package

begin_comment
comment|/**  * Copied from Olingo2 core package.  */
end_comment

begin_enum
DECL|enum|SystemQueryOption
specifier|public
enum|enum
name|SystemQueryOption
block|{
DECL|enumConstant|$format
DECL|enumConstant|$filter
DECL|enumConstant|$inlinecount
DECL|enumConstant|$orderby
DECL|enumConstant|$skiptoken
DECL|enumConstant|$skip
DECL|enumConstant|$top
DECL|enumConstant|$expand
DECL|enumConstant|$select
name|$format
block|,
name|$filter
block|,
name|$inlinecount
block|,
name|$orderby
block|,
name|$skiptoken
block|,
name|$skip
block|,
name|$top
block|,
name|$expand
block|,
name|$select
block|; }
end_enum

end_unit

