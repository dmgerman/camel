begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
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
name|util
operator|.
name|Date
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
name|util
operator|.
name|iri
operator|.
name|IRISyntaxException
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
name|impl
operator|.
name|DefaultProducer
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
name|util
operator|.
name|ExchangeHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|AtomProducer
specifier|public
class|class
name|AtomProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|AtomProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|AtomEndpoint
name|endpoint
decl_stmt|;
DECL|method|AtomProducer (AtomEndpoint endpoint)
specifier|public
name|AtomProducer
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
init|=
name|getDocument
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// now lets write the document...
name|OutputStream
name|out
init|=
name|endpoint
operator|.
name|createProducerOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|document
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|out
argument_list|,
literal|"Atom document output stream"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDocument (Exchange exchange)
specifier|protected
name|Document
argument_list|<
name|Feed
argument_list|>
name|getDocument
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IRISyntaxException
throws|,
name|IOException
block|{
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
init|=
name|endpoint
operator|.
name|parseDocument
argument_list|()
decl_stmt|;
name|Feed
name|root
init|=
name|document
operator|.
name|getRoot
argument_list|()
decl_stmt|;
name|Entry
name|entry
init|=
name|root
operator|.
name|addEntry
argument_list|()
decl_stmt|;
name|entry
operator|.
name|setPublished
argument_list|(
name|ExchangeHelper
operator|.
name|getExchangeProperty
argument_list|(
name|exchange
argument_list|,
literal|"org.apache.camel.atom.published"
argument_list|,
name|Date
operator|.
name|class
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|id
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.atom.id"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|String
name|content
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.atom.content"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setContent
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
name|String
name|summary
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.atom.summary"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|summary
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setSummary
argument_list|(
name|summary
argument_list|)
expr_stmt|;
block|}
name|String
name|title
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"org.apache.camel.atom.title"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
block|}
comment|// TODO categories, authors etc
return|return
name|document
return|;
block|}
block|}
end_class

end_unit

