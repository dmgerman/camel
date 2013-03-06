begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
operator|.
name|support
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
name|Processor
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observer
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|util
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_comment
comment|/**  * A {@link Processor} which invokes an underling {@link Observer} as messages  * arrive using hte given function to convert the {@link Exchange} to the required  * object  */
end_comment

begin_class
DECL|class|ProcessorToObserver
specifier|public
class|class
name|ProcessorToObserver
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Processor
block|{
DECL|field|func
specifier|private
specifier|final
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|func
decl_stmt|;
DECL|field|observer
specifier|private
specifier|final
name|Observer
argument_list|<
name|T
argument_list|>
name|observer
decl_stmt|;
DECL|method|ProcessorToObserver (Func1<Exchange, T> func, Observer<T> observer)
specifier|public
name|ProcessorToObserver
parameter_list|(
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|func
parameter_list|,
name|Observer
argument_list|<
name|T
argument_list|>
name|observer
parameter_list|)
block|{
name|this
operator|.
name|func
operator|=
name|func
expr_stmt|;
name|this
operator|.
name|observer
operator|=
name|observer
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
throws|throws
name|Exception
block|{
name|Exception
name|exception
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|exception
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|observer
operator|.
name|onError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|T
name|value
init|=
name|func
operator|.
name|call
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|observer
operator|.
name|onNext
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

