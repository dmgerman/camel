begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Root element of Simple Stored Procedure Template AST.  */
end_comment

begin_class
DECL|class|Template
specifier|public
class|class
name|Template
block|{
DECL|field|parameterList
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|parameterList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|procedureName
specifier|private
name|String
name|procedureName
decl_stmt|;
DECL|method|addParameter (Object parameter)
specifier|public
name|void
name|addParameter
parameter_list|(
name|Object
name|parameter
parameter_list|)
block|{
name|parameterList
operator|.
name|add
argument_list|(
name|parameter
argument_list|)
expr_stmt|;
block|}
DECL|method|getProcedureName ()
specifier|public
name|String
name|getProcedureName
parameter_list|()
block|{
return|return
name|procedureName
return|;
block|}
DECL|method|setProcedureName (String procedureName)
specifier|public
name|void
name|setProcedureName
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|procedureName
expr_stmt|;
block|}
DECL|method|getParameterList ()
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getParameterList
parameter_list|()
block|{
return|return
name|parameterList
return|;
block|}
block|}
end_class

end_unit

