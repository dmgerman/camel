begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|CamelException
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
name|Processor
import|;
end_import

begin_class
DECL|class|LoopTestProcessor
specifier|public
class|class
name|LoopTestProcessor
implements|implements
name|Processor
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|field|index
specifier|private
name|int
name|index
decl_stmt|;
DECL|method|LoopTestProcessor ()
specifier|public
name|LoopTestProcessor
parameter_list|()
block|{     }
DECL|method|LoopTestProcessor (int count)
specifier|public
name|LoopTestProcessor
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|setCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
DECL|method|setCount (int count)
specifier|public
name|void
name|setCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|this
operator|.
name|index
operator|=
literal|0
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Integer
name|c
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_SIZE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Integer
name|i
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_INDEX
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
operator|||
name|c
operator|.
name|intValue
argument_list|()
operator|!=
name|this
operator|.
name|count
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Invalid count value.  Expected "
operator|+
name|this
operator|.
name|count
operator|+
literal|" but was "
operator|+
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
literal|null
operator|||
name|i
operator|.
name|intValue
argument_list|()
operator|!=
name|this
operator|.
name|index
operator|++
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Invalid index value.  Expected "
operator|+
name|this
operator|.
name|index
operator|+
literal|" but was "
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

