begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
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
name|Endpoint
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|CryptoCmsProducer
specifier|public
class|class
name|CryptoCmsProducer
extends|extends
name|DefaultProducer
block|{
comment|// private static final Logger log =
comment|// LoggerFactory.getLogger(CmsProducer.class);
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|method|CryptoCmsProducer (Endpoint endpoint, Processor processor)
specifier|public
name|CryptoCmsProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
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
comment|// NOPMD a
comment|// processor can
comment|// throw any
comment|// exception
comment|// try {
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// } catch (Exception e) {
comment|// exchange.setException(e);
comment|// }
block|}
block|}
end_class

end_unit

