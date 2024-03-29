begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * Just a demo bean to show we are ready to process each record polled from the  * database.  */
end_comment

begin_class
DECL|class|RecordProcessor
specifier|public
class|class
name|RecordProcessor
implements|implements
name|Processor
block|{
DECL|field|log
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RecordProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange msg)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|msg
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing msg {}"
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|record
init|=
name|msg
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Processing record {}"
argument_list|,
name|record
argument_list|)
expr_stmt|;
comment|// Do something useful with this record.
block|}
block|}
end_class

end_unit

