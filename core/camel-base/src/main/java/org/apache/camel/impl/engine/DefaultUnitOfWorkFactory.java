begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
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
name|spi
operator|.
name|UnitOfWork
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
name|UnitOfWorkFactory
import|;
end_import

begin_comment
comment|/**  * Default {@link org.apache.camel.spi.UnitOfWorkFactory}  */
end_comment

begin_class
DECL|class|DefaultUnitOfWorkFactory
specifier|public
class|class
name|DefaultUnitOfWorkFactory
implements|implements
name|UnitOfWorkFactory
block|{
annotation|@
name|Override
DECL|method|createUnitOfWork (Exchange exchange)
specifier|public
name|UnitOfWork
name|createUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|UnitOfWork
name|answer
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|isUseMDCLogging
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|MDCUnitOfWork
argument_list|(
name|exchange
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getMDCLoggingKeysPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|DefaultUnitOfWork
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

