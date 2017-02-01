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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|OperationNotSupportedException
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
name|ScheduledPollEndpoint
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
comment|/**  * The jt400 component allows you to exchanges messages with an AS/400 system using data queues or program call.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.5.0"
argument_list|,
name|scheme
operator|=
literal|"jt400"
argument_list|,
name|title
operator|=
literal|"JT400"
argument_list|,
name|syntax
operator|=
literal|"jt400:userID:password/systemName/objectPath.type"
argument_list|,
name|consumerClass
operator|=
name|Jt400DataQueueConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|Jt400Endpoint
specifier|public
class|class
name|Jt400Endpoint
extends|extends
name|ScheduledPollEndpoint
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
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|Jt400Configuration
name|configuration
decl_stmt|;
comment|/**      * Creates a new AS/400 data queue endpoint using a default connection pool      * provided by the component.      *       * @throws NullPointerException if {@code component} is null      */
DECL|method|Jt400Endpoint (String endpointUri, Jt400Component component)
specifier|protected
name|Jt400Endpoint
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
DECL|method|Jt400Endpoint (String endpointUri, Jt400Component component, AS400ConnectionPool connectionPool)
specifier|protected
name|Jt400Endpoint
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
name|configuration
operator|=
operator|new
name|Jt400Configuration
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
if|if
condition|(
name|Jt400Type
operator|.
name|DTAQ
operator|==
name|configuration
operator|.
name|getType
argument_list|()
condition|)
block|{
return|return
operator|new
name|Jt400DataQueueProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|Jt400PgmProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
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
if|if
condition|(
name|Jt400Type
operator|.
name|DTAQ
operator|==
name|configuration
operator|.
name|getType
argument_list|()
condition|)
block|{
name|Consumer
name|consumer
init|=
operator|new
name|Jt400DataQueueConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|OperationNotSupportedException
argument_list|()
throw|;
block|}
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// cannot be singleton as we store an AS400 instance on the configuration
return|return
literal|false
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
name|configuration
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
name|configuration
operator|.
name|releaseConnection
argument_list|(
name|system
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the fully qualified integrated file system path name of the data      * queue of this endpoint.      *       * @return the fully qualified integrated file system path name of the data      *         queue of this endpoint      */
DECL|method|getObjectPath ()
specifier|protected
name|String
name|getObjectPath
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getObjectPath
argument_list|()
return|;
block|}
DECL|method|getType ()
specifier|public
name|Jt400Type
name|getType
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getType
argument_list|()
return|;
block|}
DECL|method|setType (Jt400Type type)
specifier|public
name|void
name|setType
parameter_list|(
name|Jt400Type
name|type
parameter_list|)
block|{
name|configuration
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|getSearchKey ()
specifier|public
name|String
name|getSearchKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSearchKey
argument_list|()
return|;
block|}
DECL|method|isKeyed ()
specifier|public
name|boolean
name|isKeyed
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isKeyed
argument_list|()
return|;
block|}
DECL|method|getOutputFieldsIdxArray ()
specifier|public
name|Integer
index|[]
name|getOutputFieldsIdxArray
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOutputFieldsIdxArray
argument_list|()
return|;
block|}
DECL|method|getCcsid ()
specifier|public
name|int
name|getCcsid
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCcsid
argument_list|()
return|;
block|}
DECL|method|setOutputFieldsIdxArray (Integer[] outputFieldsIdxArray)
specifier|public
name|void
name|setOutputFieldsIdxArray
parameter_list|(
name|Integer
index|[]
name|outputFieldsIdxArray
parameter_list|)
block|{
name|configuration
operator|.
name|setOutputFieldsIdxArray
argument_list|(
name|outputFieldsIdxArray
argument_list|)
expr_stmt|;
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
name|configuration
operator|.
name|setSearchKey
argument_list|(
name|searchKey
argument_list|)
expr_stmt|;
block|}
DECL|method|setOutputFieldsIdx (String outputFieldsIdx)
specifier|public
name|void
name|setOutputFieldsIdx
parameter_list|(
name|String
name|outputFieldsIdx
parameter_list|)
block|{
name|configuration
operator|.
name|setOutputFieldsIdx
argument_list|(
name|outputFieldsIdx
argument_list|)
expr_stmt|;
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
name|configuration
operator|.
name|setKeyed
argument_list|(
name|keyed
argument_list|)
expr_stmt|;
block|}
DECL|method|getOutputFieldsLengthArray ()
specifier|public
name|Integer
index|[]
name|getOutputFieldsLengthArray
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOutputFieldsLengthArray
argument_list|()
return|;
block|}
DECL|method|setSearchType (Jt400Configuration.SearchType searchType)
specifier|public
name|void
name|setSearchType
parameter_list|(
name|Jt400Configuration
operator|.
name|SearchType
name|searchType
parameter_list|)
block|{
name|configuration
operator|.
name|setSearchType
argument_list|(
name|searchType
argument_list|)
expr_stmt|;
block|}
DECL|method|isGuiAvailable ()
specifier|public
name|boolean
name|isGuiAvailable
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isGuiAvailable
argument_list|()
return|;
block|}
DECL|method|setFormat (Jt400Configuration.Format format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|Jt400Configuration
operator|.
name|Format
name|format
parameter_list|)
block|{
name|configuration
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
DECL|method|setFieldsLength (String fieldsLength)
specifier|public
name|void
name|setFieldsLength
parameter_list|(
name|String
name|fieldsLength
parameter_list|)
block|{
name|configuration
operator|.
name|setFieldsLength
argument_list|(
name|fieldsLength
argument_list|)
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|Jt400Configuration
operator|.
name|Format
name|getFormat
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getFormat
argument_list|()
return|;
block|}
DECL|method|setOutputFieldsLengthArray (Integer[] outputFieldsLengthArray)
specifier|public
name|void
name|setOutputFieldsLengthArray
parameter_list|(
name|Integer
index|[]
name|outputFieldsLengthArray
parameter_list|)
block|{
name|configuration
operator|.
name|setOutputFieldsLengthArray
argument_list|(
name|outputFieldsLengthArray
argument_list|)
expr_stmt|;
block|}
DECL|method|getCssid ()
specifier|public
name|int
name|getCssid
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCssid
argument_list|()
return|;
block|}
DECL|method|getUserID ()
specifier|public
name|String
name|getUserID
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUserID
argument_list|()
return|;
block|}
DECL|method|getSearchType ()
specifier|public
name|Jt400Configuration
operator|.
name|SearchType
name|getSearchType
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSearchType
argument_list|()
return|;
block|}
DECL|method|setCcsid (int ccsid)
specifier|public
name|void
name|setCcsid
parameter_list|(
name|int
name|ccsid
parameter_list|)
block|{
name|configuration
operator|.
name|setCcsid
argument_list|(
name|ccsid
argument_list|)
expr_stmt|;
block|}
DECL|method|setGuiAvailable (boolean guiAvailable)
specifier|public
name|void
name|setGuiAvailable
parameter_list|(
name|boolean
name|guiAvailable
parameter_list|)
block|{
name|configuration
operator|.
name|setGuiAvailable
argument_list|(
name|guiAvailable
argument_list|)
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getPassword
argument_list|()
return|;
block|}
DECL|method|getSystemName ()
specifier|public
name|String
name|getSystemName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSystemName
argument_list|()
return|;
block|}
DECL|method|isFieldIdxForOuput (int idx)
specifier|public
name|boolean
name|isFieldIdxForOuput
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|binarySearch
argument_list|(
name|getOutputFieldsIdxArray
argument_list|()
argument_list|,
name|idx
argument_list|)
operator|>=
literal|0
return|;
block|}
DECL|method|getOutputFieldLength (int idx)
specifier|public
name|int
name|getOutputFieldLength
parameter_list|(
name|int
name|idx
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|getOutputFieldsLengthArray
argument_list|()
index|[
name|idx
index|]
return|;
block|}
DECL|method|setObjectPath (String objectPath)
specifier|public
name|void
name|setObjectPath
parameter_list|(
name|String
name|objectPath
parameter_list|)
block|{
name|configuration
operator|.
name|setObjectPath
argument_list|(
name|objectPath
argument_list|)
expr_stmt|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|configuration
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
DECL|method|setUserID (String userID)
specifier|public
name|void
name|setUserID
parameter_list|(
name|String
name|userID
parameter_list|)
block|{
name|configuration
operator|.
name|setUserID
argument_list|(
name|userID
argument_list|)
expr_stmt|;
block|}
DECL|method|setSystemName (String systemName)
specifier|public
name|void
name|setSystemName
parameter_list|(
name|String
name|systemName
parameter_list|)
block|{
name|configuration
operator|.
name|setSystemName
argument_list|(
name|systemName
argument_list|)
expr_stmt|;
block|}
DECL|method|setSecured (boolean secured)
specifier|public
name|void
name|setSecured
parameter_list|(
name|boolean
name|secured
parameter_list|)
block|{
name|configuration
operator|.
name|setSecured
argument_list|(
name|secured
argument_list|)
expr_stmt|;
block|}
DECL|method|isSecured ()
specifier|public
name|boolean
name|isSecured
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isSecured
argument_list|()
return|;
block|}
DECL|method|getReadTimeout ()
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReadTimeout
argument_list|()
return|;
block|}
DECL|method|setReadTimeout (int readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|readTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

