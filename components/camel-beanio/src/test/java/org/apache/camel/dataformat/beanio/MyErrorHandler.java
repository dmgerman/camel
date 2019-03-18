begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|InvalidRecordException
import|;
end_import

begin_class
DECL|class|MyErrorHandler
specifier|public
class|class
name|MyErrorHandler
extends|extends
name|BeanIOErrorHandler
block|{
DECL|method|MyErrorHandler ()
specifier|public
name|MyErrorHandler
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|invalidRecord (InvalidRecordException ex)
specifier|public
name|void
name|invalidRecord
parameter_list|(
name|InvalidRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|line
init|=
literal|"ExchangeId: "
operator|+
name|id
operator|+
literal|" Invalid record: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|line
argument_list|)
expr_stmt|;
comment|// lets handle the error and store to the results a dummy error DTO
name|MyErrorDto
name|dto
init|=
operator|new
name|MyErrorDto
argument_list|(
name|ex
operator|.
name|getRecordName
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|handleErrorAndAddAsResult
argument_list|(
name|dto
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

