begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyVetoException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400ConnectionPool
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|BaseDataQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|DataQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|KeyedDataQueue
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
name|CamelException
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
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * AS/400 Data queue endpoint  */
end_comment

begin_class
DECL|class|Jt400DataQueueEndpoint
specifier|public
class|class
name|Jt400DataQueueEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"KEY"
decl_stmt|;
DECL|field|SENDER_INFORMATION
specifier|public
specifier|static
specifier|final
name|String
name|SENDER_INFORMATION
init|=
literal|"SENDER_INFORMATION"
decl_stmt|;
comment|/**      * SearchTypes for reading from Keyed Data Queues      */
DECL|enum|SearchType
specifier|public
enum|enum
name|SearchType
block|{
DECL|enumConstant|EQ
DECL|enumConstant|NE
DECL|enumConstant|LT
DECL|enumConstant|LE
DECL|enumConstant|GT
DECL|enumConstant|GE
name|EQ
block|,
name|NE
block|,
name|LT
block|,
name|LE
block|,
name|GT
block|,
name|GE
block|;     }
comment|/**      * Enumeration of supported data formats      */
DECL|enum|Format
specifier|public
enum|enum
name|Format
block|{
comment|/**          * Using<code>String</code> for transferring data          */
DECL|enumConstant|text
name|text
block|,
comment|/**          * Using<code>byte[]</code> for transferring data          */
DECL|enumConstant|binary
name|binary
block|;     }
comment|/**      * Encapsulates the base endpoint options and functionality.      */
DECL|field|baseEndpoint
specifier|private
specifier|final
name|Jt400Endpoint
name|baseEndpoint
decl_stmt|;
comment|/**      * @deprecated Used by {@link #getDataQueue()}, which is deprecated.      */
annotation|@
name|Deprecated
DECL|field|dataQueue
specifier|private
name|BaseDataQueue
name|dataQueue
decl_stmt|;
DECL|field|keyed
specifier|private
name|boolean
name|keyed
decl_stmt|;
DECL|field|searchKey
specifier|private
name|String
name|searchKey
decl_stmt|;
DECL|field|searchType
specifier|private
name|SearchType
name|searchType
init|=
name|SearchType
operator|.
name|EQ
decl_stmt|;
comment|/**      * Creates a new AS/400 data queue endpoint using a default connection pool      * provided by the component.      *       * @throws NullPointerException if {@code component} is null      */
DECL|method|Jt400DataQueueEndpoint (String endpointUri, Jt400Component component)
specifier|protected
name|Jt400DataQueueEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Jt400Component
name|component
parameter_list|)
throws|throws
name|CamelException
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|component
operator|.
name|getConnectionPool
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new AS/400 data queue endpoint using the specified connection      * pool.      */
DECL|method|Jt400DataQueueEndpoint (String endpointUri, Jt400Component component, AS400ConnectionPool connectionPool)
specifier|protected
name|Jt400DataQueueEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Jt400Component
name|component
parameter_list|,
name|AS400ConnectionPool
name|connectionPool
parameter_list|)
throws|throws
name|CamelException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connectionPool
argument_list|,
literal|"connectionPool"
argument_list|)
expr_stmt|;
try|try
block|{
name|baseEndpoint
operator|=
operator|new
name|Jt400Endpoint
argument_list|(
name|endpointUri
argument_list|,
name|connectionPool
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Unable to parse URI for "
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpointUri
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setCcsid (int ccsid)
specifier|public
name|void
name|setCcsid
parameter_list|(
name|int
name|ccsid
parameter_list|)
throws|throws
name|PropertyVetoException
block|{
name|baseEndpoint
operator|.
name|setCcsid
argument_list|(
name|ccsid
argument_list|)
expr_stmt|;
block|}
DECL|method|setFormat (Format format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|Format
name|format
parameter_list|)
block|{
name|baseEndpoint
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|Format
name|getFormat
parameter_list|()
block|{
return|return
name|baseEndpoint
operator|.
name|getFormat
argument_list|()
return|;
block|}
DECL|method|setKeyed (boolean keyed)
specifier|public
name|void
name|setKeyed
parameter_list|(
name|boolean
name|keyed
parameter_list|)
block|{
name|this
operator|.
name|keyed
operator|=
name|keyed
expr_stmt|;
block|}
DECL|method|isKeyed ()
specifier|public
name|boolean
name|isKeyed
parameter_list|()
block|{
return|return
name|keyed
return|;
block|}
DECL|method|setSearchKey (String searchKey)
specifier|public
name|void
name|setSearchKey
parameter_list|(
name|String
name|searchKey
parameter_list|)
block|{
name|this
operator|.
name|searchKey
operator|=
name|searchKey
expr_stmt|;
block|}
DECL|method|getSearchKey ()
specifier|public
name|String
name|getSearchKey
parameter_list|()
block|{
return|return
name|searchKey
return|;
block|}
DECL|method|setSearchType (SearchType searchType)
specifier|public
name|void
name|setSearchType
parameter_list|(
name|SearchType
name|searchType
parameter_list|)
block|{
name|this
operator|.
name|searchType
operator|=
name|searchType
expr_stmt|;
block|}
DECL|method|getSearchType ()
specifier|public
name|SearchType
name|getSearchType
parameter_list|()
block|{
return|return
name|searchType
return|;
block|}
DECL|method|setGuiAvailable (boolean guiAvailable)
specifier|public
name|void
name|setGuiAvailable
parameter_list|(
name|boolean
name|guiAvailable
parameter_list|)
throws|throws
name|PropertyVetoException
block|{
name|baseEndpoint
operator|.
name|setGuiAvailable
argument_list|(
name|guiAvailable
argument_list|)
expr_stmt|;
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
return|return
operator|new
name|Jt400DataQueueConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Jt400DataQueueProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Obtains an {@code AS400} object that connects to this endpoint. Since      * these objects represent limited resources, clients have the      * responsibility of {@link #releaseSystem(AS400) releasing them} when done.      *       * @return an {@code AS400} object that connects to this endpoint      */
DECL|method|getSystem ()
specifier|protected
name|AS400
name|getSystem
parameter_list|()
block|{
return|return
name|baseEndpoint
operator|.
name|getConnection
argument_list|()
return|;
block|}
comment|/**      * Releases a previously obtained {@code AS400} object from use.      *       * @param system a previously obtained {@code AS400} object      */
DECL|method|releaseSystem (AS400 system)
specifier|protected
name|void
name|releaseSystem
parameter_list|(
name|AS400
name|system
parameter_list|)
block|{
name|baseEndpoint
operator|.
name|releaseConnection
argument_list|(
name|system
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated This method does not benefit from connection pooling; data      *             queue instances should be constructed with a connection      *             obtained by {@link #getSystem()}.      */
annotation|@
name|Deprecated
DECL|method|getDataQueue ()
specifier|protected
name|BaseDataQueue
name|getDataQueue
parameter_list|()
block|{
if|if
condition|(
name|dataQueue
operator|==
literal|null
condition|)
block|{
name|AS400
name|system
init|=
operator|new
name|AS400
argument_list|(
name|baseEndpoint
operator|.
name|getSystemName
argument_list|()
argument_list|,
name|baseEndpoint
operator|.
name|getUserID
argument_list|()
argument_list|,
name|baseEndpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|objectPath
init|=
name|baseEndpoint
operator|.
name|getObjectPath
argument_list|()
decl_stmt|;
name|dataQueue
operator|=
name|keyed
condition|?
operator|new
name|KeyedDataQueue
argument_list|(
name|system
argument_list|,
name|objectPath
argument_list|)
else|:
operator|new
name|DataQueue
argument_list|(
name|system
argument_list|,
name|objectPath
argument_list|)
expr_stmt|;
block|}
return|return
name|dataQueue
return|;
block|}
comment|/**      * Returns the fully qualified integrated file system path name of the data      * queue of this endpoint.      *       * @return the fully qualified integrated file system path name of the data      *         queue of this endpoint      */
DECL|method|getObjectPath ()
specifier|protected
name|String
name|getObjectPath
parameter_list|()
block|{
return|return
name|baseEndpoint
operator|.
name|getObjectPath
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

