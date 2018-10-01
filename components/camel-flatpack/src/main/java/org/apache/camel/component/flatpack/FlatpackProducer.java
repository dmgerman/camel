begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DataSet
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|Parser
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

begin_class
DECL|class|FlatpackProducer
class|class
name|FlatpackProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|FlatpackEndpoint
name|endpoint
decl_stmt|;
DECL|method|FlatpackProducer (FlatpackEndpoint endpoint)
name|FlatpackProducer
parameter_list|(
name|FlatpackEndpoint
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Parser
name|parser
init|=
name|endpoint
operator|.
name|createParser
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|DataSet
name|dataSet
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataSet
operator|.
name|getErrorCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Flatpack has found %s errors while parsing"
argument_list|,
name|dataSet
operator|.
name|getErrorCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|FlatpackException
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|dataSet
operator|.
name|getErrors
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSplitRows
argument_list|()
condition|)
block|{
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|dataSet
operator|.
name|next
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|processDataSet
argument_list|(
name|exchange
argument_list|,
name|dataSet
argument_list|,
name|counter
operator|++
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|endpoint
operator|.
name|processDataSet
argument_list|(
name|exchange
argument_list|,
name|dataSet
argument_list|,
name|dataSet
operator|.
name|getRowCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

