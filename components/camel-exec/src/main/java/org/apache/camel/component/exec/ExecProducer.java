begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
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
name|impl
operator|.
name|DefaultProducer
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Exec producer.  *   * @see {link Producer}  */
end_comment

begin_class
DECL|class|ExecProducer
specifier|public
class|class
name|ExecProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|ExecEndpoint
name|endpoint
decl_stmt|;
DECL|method|ExecProducer (ExecEndpoint endpoint)
specifier|public
name|ExecProducer
parameter_list|(
name|ExecEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ExecProducer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ExecCommand
name|execCommand
init|=
name|getBinding
argument_list|()
operator|.
name|readInput
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Executing "
operator|+
name|execCommand
argument_list|)
expr_stmt|;
block|}
name|ExecResult
name|result
init|=
name|endpoint
operator|.
name|getCommandExecutor
argument_list|()
operator|.
name|execute
argument_list|(
name|execCommand
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|result
argument_list|,
literal|"The command executor must return a not-null result"
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"The command "
operator|+
name|execCommand
operator|+
literal|" had exit value "
operator|+
name|result
operator|.
name|getExitValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|log
operator|.
name|isErrorEnabled
argument_list|()
operator|&&
name|result
operator|.
name|getExitValue
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"The command "
operator|+
name|execCommand
operator|+
literal|" returned exit value "
operator|+
name|result
operator|.
name|getExitValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getBinding
argument_list|()
operator|.
name|writeOutput
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|private
name|ExecBinding
name|getBinding
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getBinding
argument_list|()
return|;
block|}
block|}
end_class

end_unit

