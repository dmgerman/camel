begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lucene
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lucene
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|LuceneIndexProducer
specifier|public
class|class
name|LuceneIndexProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|config
name|LuceneConfiguration
name|config
decl_stmt|;
DECL|field|indexer
name|LuceneIndexer
name|indexer
decl_stmt|;
DECL|method|LuceneIndexProducer (Endpoint endpoint, LuceneConfiguration config, LuceneIndexer indexer)
specifier|public
name|LuceneIndexProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|LuceneConfiguration
name|config
parameter_list|,
name|LuceneIndexer
name|indexer
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|indexer
operator|.
name|getNiofsDirectory
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
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
name|indexer
operator|.
name|index
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|LuceneConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (LuceneConfiguration config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|LuceneConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getIndexer ()
specifier|public
name|LuceneIndexer
name|getIndexer
parameter_list|()
block|{
return|return
name|indexer
return|;
block|}
DECL|method|setIndexer (LuceneIndexer indexer)
specifier|public
name|void
name|setIndexer
parameter_list|(
name|LuceneIndexer
name|indexer
parameter_list|)
block|{
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
block|}
block|}
end_class

end_unit

