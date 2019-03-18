begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
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
operator|.
name|strategy
package|;
end_package

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|GenericFileExpressionRenamer
specifier|public
class|class
name|GenericFileExpressionRenamer
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericFileRenamer
argument_list|<
name|T
argument_list|>
block|{
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|method|GenericFileExpressionRenamer ()
specifier|public
name|GenericFileExpressionRenamer
parameter_list|()
block|{     }
DECL|method|GenericFileExpressionRenamer (Expression expression)
specifier|public
name|GenericFileExpressionRenamer
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|renameFile (Exchange exchange, GenericFile<T> file)
specifier|public
name|GenericFile
argument_list|<
name|T
argument_list|>
name|renameFile
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|String
name|newName
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// make a copy as result and change its file name
name|GenericFile
argument_list|<
name|T
argument_list|>
name|result
init|=
name|file
operator|.
name|copyFrom
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|result
operator|.
name|changeFileName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
block|}
end_class

end_unit

