begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
comment|/**  * A {@link org.apache.camel.spi.UnitOfWork} failed with a number of caused exceptions.  *<p/>  * This implementation will provide the first exception from the list in its cause, so its shown  * in the stacktrace etc when logging this exception. But the remainder exceptions is only available  * from the {@link #getCauses()} method.  */
end_comment

begin_class
DECL|class|CamelUnitOfWorkException
specifier|public
class|class
name|CamelUnitOfWorkException
extends|extends
name|CamelExchangeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|causes
specifier|private
specifier|final
name|List
argument_list|<
name|Exception
argument_list|>
name|causes
decl_stmt|;
DECL|method|CamelUnitOfWorkException (Exchange exchange, List<Exception> causes)
specifier|public
name|CamelUnitOfWorkException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|Exception
argument_list|>
name|causes
parameter_list|)
block|{
comment|// just provide the first exception as cause, as it will be logged in the stacktraces
name|super
argument_list|(
literal|"Unit of work failed on exchange with "
operator|+
name|causes
operator|.
name|size
argument_list|()
operator|+
literal|" caused exceptions. First exception provided as cause to this exception."
argument_list|,
name|exchange
argument_list|,
name|causes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|causes
operator|=
name|causes
expr_stmt|;
block|}
DECL|method|getCauses ()
specifier|public
name|List
argument_list|<
name|Exception
argument_list|>
name|getCauses
parameter_list|()
block|{
return|return
name|causes
return|;
block|}
block|}
end_class

end_unit

