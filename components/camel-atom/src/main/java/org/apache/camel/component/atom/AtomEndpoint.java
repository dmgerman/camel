begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|Abdera
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|factory
operator|.
name|Factory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|Parser
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
name|PollingConsumer
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
name|DefaultPollingEndpoint
import|;
end_import

begin_comment
comment|/**  * An<a href="http://activemq.apache.org/camel/atom.html">Atom Endpoint</a>.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|AtomEndpoint
specifier|public
class|class
name|AtomEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|atomFactory
specifier|private
name|Factory
name|atomFactory
decl_stmt|;
DECL|field|atomUri
specifier|private
name|String
name|atomUri
decl_stmt|;
DECL|field|splitEntries
specifier|private
name|boolean
name|splitEntries
init|=
literal|true
decl_stmt|;
DECL|method|AtomEndpoint (String endpointUri, AtomComponent component, String atomUri)
specifier|public
name|AtomEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AtomComponent
name|component
parameter_list|,
name|String
name|atomUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomUri
operator|=
name|atomUri
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
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|AtomProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isSplitEntries
argument_list|()
condition|)
block|{
return|return
operator|new
name|AtomEntryPollingConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|AtomPollingConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
DECL|method|parseDocument ()
specifier|public
name|Document
argument_list|<
name|Feed
argument_list|>
name|parseDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
name|getAtomUri
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
return|return
name|createAtomParser
argument_list|()
operator|.
name|parse
argument_list|(
name|in
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|createProducerOutputStream ()
specifier|public
name|OutputStream
name|createProducerOutputStream
parameter_list|()
throws|throws
name|FileNotFoundException
block|{
return|return
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|getAtomUri
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExchange (Document<Feed> document, Entry entry)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
parameter_list|,
name|Entry
name|entry
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"CamelAtomFeed"
argument_list|,
name|document
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getAtomFactory ()
specifier|public
name|Factory
name|getAtomFactory
parameter_list|()
block|{
if|if
condition|(
name|atomFactory
operator|==
literal|null
condition|)
block|{
name|atomFactory
operator|=
name|createAtomFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|atomFactory
return|;
block|}
DECL|method|setAtomFactory (Factory atomFactory)
specifier|public
name|void
name|setAtomFactory
parameter_list|(
name|Factory
name|atomFactory
parameter_list|)
block|{
name|this
operator|.
name|atomFactory
operator|=
name|atomFactory
expr_stmt|;
block|}
DECL|method|getAtomUri ()
specifier|public
name|String
name|getAtomUri
parameter_list|()
block|{
return|return
name|atomUri
return|;
block|}
DECL|method|setAtomUri (String atomUri)
specifier|public
name|void
name|setAtomUri
parameter_list|(
name|String
name|atomUri
parameter_list|)
block|{
name|this
operator|.
name|atomUri
operator|=
name|atomUri
expr_stmt|;
block|}
DECL|method|isSplitEntries ()
specifier|public
name|boolean
name|isSplitEntries
parameter_list|()
block|{
return|return
name|splitEntries
return|;
block|}
comment|/**      * Sets whether or not entries should be sent individually or whether the entire      * feed should be sent as a single message      */
DECL|method|setSplitEntries (boolean splitEntries)
specifier|public
name|void
name|setSplitEntries
parameter_list|(
name|boolean
name|splitEntries
parameter_list|)
block|{
name|this
operator|.
name|splitEntries
operator|=
name|splitEntries
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createAtomFactory ()
specifier|protected
name|Factory
name|createAtomFactory
parameter_list|()
block|{
return|return
name|Abdera
operator|.
name|getNewFactory
argument_list|()
return|;
block|}
DECL|method|createAtomParser ()
specifier|protected
name|Parser
name|createAtomParser
parameter_list|()
block|{
return|return
name|Abdera
operator|.
name|getNewParser
argument_list|()
return|;
block|}
block|}
end_class

end_unit

