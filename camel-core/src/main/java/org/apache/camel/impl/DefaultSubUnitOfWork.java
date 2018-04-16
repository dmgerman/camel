begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|spi
operator|.
name|SubUnitOfWork
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
name|spi
operator|.
name|SubUnitOfWorkCallback
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link org.apache.camel.spi.SubUnitOfWork} combined  * with a {@link SubUnitOfWorkCallback} to gather callbacks into this {@link SubUnitOfWork} state  */
end_comment

begin_class
DECL|class|DefaultSubUnitOfWork
specifier|public
class|class
name|DefaultSubUnitOfWork
implements|implements
name|SubUnitOfWork
implements|,
name|SubUnitOfWorkCallback
block|{
DECL|field|failedExceptions
specifier|private
name|List
argument_list|<
name|Exception
argument_list|>
name|failedExceptions
decl_stmt|;
DECL|field|failed
specifier|private
name|boolean
name|failed
decl_stmt|;
annotation|@
name|Override
DECL|method|onExhausted (Exchange exchange)
specifier|public
name|void
name|onExhausted
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addFailedException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|failed
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onDone (Exchange exchange)
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|addFailedException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|failed
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isFailed ()
specifier|public
name|boolean
name|isFailed
parameter_list|()
block|{
return|return
name|failed
return|;
block|}
annotation|@
name|Override
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|Exception
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|failedExceptions
return|;
block|}
DECL|method|addFailedException (Exception exception)
specifier|private
name|void
name|addFailedException
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
if|if
condition|(
name|failedExceptions
operator|==
literal|null
condition|)
block|{
name|failedExceptions
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|failedExceptions
operator|.
name|contains
argument_list|(
name|exception
argument_list|)
condition|)
block|{
comment|// avoid adding the same exception multiple times
name|failedExceptions
operator|.
name|add
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

