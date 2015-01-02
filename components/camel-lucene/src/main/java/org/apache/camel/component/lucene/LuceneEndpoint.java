begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CamelContext
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
name|Component
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
name|Consumer
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriParam
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"lucene"
argument_list|,
name|label
operator|=
literal|"database,search"
argument_list|)
DECL|class|LuceneEndpoint
specifier|public
class|class
name|LuceneEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|config
name|LuceneConfiguration
name|config
decl_stmt|;
DECL|field|indexer
name|LuceneIndexer
name|indexer
decl_stmt|;
DECL|field|insertFlag
name|boolean
name|insertFlag
decl_stmt|;
DECL|method|LuceneEndpoint ()
specifier|public
name|LuceneEndpoint
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|LuceneEndpoint (String endpointUri, CamelContext camelContext)
specifier|public
name|LuceneEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|LuceneEndpoint (String endpointUri, Component component)
specifier|public
name|LuceneEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|LuceneEndpoint (String endpointUri)
specifier|public
name|LuceneEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|LuceneEndpoint (String endpointUri, LuceneComponent component, LuceneConfiguration config)
specifier|public
name|LuceneEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|LuceneComponent
name|component
parameter_list|,
name|LuceneConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getOperation
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"insert"
argument_list|)
condition|)
block|{
name|this
operator|.
name|indexer
operator|=
operator|new
name|LuceneIndexer
argument_list|(
name|config
operator|.
name|getSourceDirectory
argument_list|()
argument_list|,
name|config
operator|.
name|getIndexDirectory
argument_list|()
argument_list|,
name|config
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
expr_stmt|;
name|insertFlag
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported for Lucene endpoint"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|insertFlag
condition|)
block|{
return|return
operator|new
name|LuceneQueryProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|config
argument_list|)
return|;
block|}
return|return
operator|new
name|LuceneIndexProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|config
argument_list|,
name|indexer
argument_list|)
return|;
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
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

