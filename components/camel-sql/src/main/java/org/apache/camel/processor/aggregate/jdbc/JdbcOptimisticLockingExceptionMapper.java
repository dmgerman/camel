begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|jdbc
package|;
end_package

begin_comment
comment|/**  * Mapper allowing different JDBC vendors to be mapped with vendor specific error codes  * to an {@link JdbcAggregationRepository.OptimisticLockingException}}.  */
end_comment

begin_interface
DECL|interface|JdbcOptimisticLockingExceptionMapper
specifier|public
interface|interface
name|JdbcOptimisticLockingExceptionMapper
block|{
comment|/**      * Checks the caused exception whether its to be considered as an {@link JdbcAggregationRepository.OptimisticLockingException}.      *      * @param cause the caused exception      * @return<tt>true</tt> if the caused should be rethrown as an {@link JdbcAggregationRepository.OptimisticLockingException}.      */
DECL|method|isOptimisticLocking (Exception cause)
name|boolean
name|isOptimisticLocking
parameter_list|(
name|Exception
name|cause
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

