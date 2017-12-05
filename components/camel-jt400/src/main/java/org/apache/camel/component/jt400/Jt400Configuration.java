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
name|URI
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
name|ConnectionPoolException
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
name|RuntimeCamelException
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
name|Metadata
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
name|spi
operator|.
name|UriParams
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
name|UriPath
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
name|lang3
operator|.
name|StringUtils
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

begin_class
annotation|@
name|UriParams
DECL|class|Jt400Configuration
specifier|public
class|class
name|Jt400Configuration
block|{
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
block|}
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
block|}
comment|/**      * Logging tool.      */
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Jt400Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constant used to specify that the default system CCSID be used (a      * negative CCSID is otherwise invalid).      */
DECL|field|DEFAULT_SYSTEM_CCSID
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_SYSTEM_CCSID
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|connectionPool
specifier|private
specifier|final
name|AS400ConnectionPool
name|connectionPool
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|userID
specifier|private
name|String
name|userID
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|systemName
specifier|private
name|String
name|systemName
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|objectPath
specifier|private
name|String
name|objectPath
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|type
specifier|private
name|Jt400Type
name|type
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ccsid
specifier|private
name|int
name|ccsid
init|=
name|DEFAULT_SYSTEM_CCSID
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"text"
argument_list|)
DECL|field|format
specifier|private
name|Format
name|format
init|=
name|Format
operator|.
name|text
decl_stmt|;
annotation|@
name|UriParam
DECL|field|guiAvailable
specifier|private
name|boolean
name|guiAvailable
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keyed
specifier|private
name|boolean
name|keyed
decl_stmt|;
annotation|@
name|UriParam
DECL|field|searchKey
specifier|private
name|String
name|searchKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"EQ"
argument_list|)
DECL|field|searchType
specifier|private
name|SearchType
name|searchType
init|=
name|SearchType
operator|.
name|EQ
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|secured
specifier|private
name|boolean
name|secured
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputFieldsIdxArray
specifier|private
name|Integer
index|[]
name|outputFieldsIdxArray
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputFieldsLengthArray
specifier|private
name|Integer
index|[]
name|outputFieldsLengthArray
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|readTimeout
specifier|private
name|int
name|readTimeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"procedureName"
argument_list|)
DECL|field|procedureName
specifier|private
name|String
name|procedureName
decl_stmt|;
DECL|method|Jt400Configuration (String endpointUri, AS400ConnectionPool connectionPool)
specifier|public
name|Jt400Configuration
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AS400ConnectionPool
name|connectionPool
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpointUri
argument_list|,
literal|"endpointUri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connectionPool
argument_list|,
literal|"connectionPool"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|String
index|[]
name|credentials
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|systemName
operator|=
name|uri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|userID
operator|=
name|credentials
index|[
literal|0
index|]
expr_stmt|;
name|password
operator|=
name|credentials
index|[
literal|1
index|]
expr_stmt|;
name|objectPath
operator|=
name|uri
operator|.
name|getPath
argument_list|()
expr_stmt|;
name|this
operator|.
name|connectionPool
operator|=
name|connectionPool
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Jt400Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Whether to work with data queues or remote program call      */
DECL|method|setType (Jt400Type type)
specifier|public
name|void
name|setType
parameter_list|(
name|Jt400Type
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns the name of the AS/400 system.      */
DECL|method|getSystemName ()
specifier|public
name|String
name|getSystemName
parameter_list|()
block|{
return|return
name|systemName
return|;
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
name|this
operator|.
name|systemName
operator|=
name|systemName
expr_stmt|;
block|}
comment|/**      * Returns the ID of the AS/400 user.      */
DECL|method|getUserID ()
specifier|public
name|String
name|getUserID
parameter_list|()
block|{
return|return
name|userID
return|;
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
name|this
operator|.
name|userID
operator|=
name|userID
expr_stmt|;
block|}
comment|/**      * Returns the password of the AS/400 user.      */
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
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
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * Returns the fully qualified integrated file system path name of the      * target object of this endpoint.      */
DECL|method|getObjectPath ()
specifier|public
name|String
name|getObjectPath
parameter_list|()
block|{
return|return
name|objectPath
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
name|this
operator|.
name|objectPath
operator|=
name|objectPath
expr_stmt|;
block|}
comment|// Options
comment|/**      * Returns the CCSID to use for the connection with the AS/400 system.      * Returns -1 if the CCSID to use is the default system CCSID.      */
DECL|method|getCssid ()
specifier|public
name|int
name|getCssid
parameter_list|()
block|{
return|return
name|ccsid
return|;
block|}
comment|/**      * Sets the CCSID to use for the connection with the AS/400 system.      */
DECL|method|setCcsid (int ccsid)
specifier|public
name|void
name|setCcsid
parameter_list|(
name|int
name|ccsid
parameter_list|)
block|{
name|this
operator|.
name|ccsid
operator|=
operator|(
name|ccsid
operator|<
literal|0
operator|)
condition|?
name|DEFAULT_SYSTEM_CCSID
else|:
name|ccsid
expr_stmt|;
block|}
comment|/**      * Returns the data format for sending messages.      */
DECL|method|getFormat ()
specifier|public
name|Format
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
comment|/**      * Sets the data format for sending messages.      */
DECL|method|setFormat (Format format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|Format
name|format
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|format
argument_list|,
literal|"format"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
comment|/**      * Returns whether AS/400 prompting is enabled in the environment running      * Camel.      */
DECL|method|isGuiAvailable ()
specifier|public
name|boolean
name|isGuiAvailable
parameter_list|()
block|{
return|return
name|guiAvailable
return|;
block|}
comment|/**      * Sets whether AS/400 prompting is enabled in the environment running      * Camel.      */
DECL|method|setGuiAvailable (boolean guiAvailable)
specifier|public
name|void
name|setGuiAvailable
parameter_list|(
name|boolean
name|guiAvailable
parameter_list|)
block|{
name|this
operator|.
name|guiAvailable
operator|=
name|guiAvailable
expr_stmt|;
block|}
DECL|method|getCcsid ()
specifier|public
name|int
name|getCcsid
parameter_list|()
block|{
return|return
name|ccsid
return|;
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
comment|/**      * Whether to use keyed or non-keyed data queues.      */
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
comment|/**      * Search key for keyed data queues.      */
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
comment|/**      * Search type such as EQ for equal etc.      */
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
DECL|method|getOutputFieldsIdxArray ()
specifier|public
name|Integer
index|[]
name|getOutputFieldsIdxArray
parameter_list|()
block|{
return|return
name|outputFieldsIdxArray
return|;
block|}
DECL|method|isSecured ()
specifier|public
name|boolean
name|isSecured
parameter_list|()
block|{
return|return
name|secured
return|;
block|}
comment|/**      * Whether connections to AS/400 are secured with SSL.      */
DECL|method|setSecured (boolean secured)
specifier|public
name|void
name|setSecured
parameter_list|(
name|boolean
name|secured
parameter_list|)
block|{
name|this
operator|.
name|secured
operator|=
name|secured
expr_stmt|;
block|}
comment|/**      * Specifies which fields (program parameters) are output parameters.      */
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
name|this
operator|.
name|outputFieldsIdxArray
operator|=
name|outputFieldsIdxArray
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
name|outputFieldsLengthArray
return|;
block|}
comment|/**      * Specifies the fields (program parameters) length as in the AS/400 program definition.      */
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
name|this
operator|.
name|outputFieldsLengthArray
operator|=
name|outputFieldsLengthArray
expr_stmt|;
block|}
DECL|method|getReadTimeout ()
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
name|readTimeout
return|;
block|}
comment|/**      * Timeout in millis the consumer will wait while trying to read a new message of the data queue.      */
DECL|method|setReadTimeout (int readTimeout)
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
DECL|method|getProcedureName ()
specifier|public
name|String
name|getProcedureName
parameter_list|()
block|{
return|return
name|procedureName
return|;
block|}
comment|/**      * Procedure name from a service program to call      */
DECL|method|setProcedureName (String procedureName)
specifier|public
name|void
name|setProcedureName
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|procedureName
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
if|if
condition|(
name|outputFieldsIdx
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|outputArray
init|=
name|outputFieldsIdx
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|outputFieldsIdxArray
operator|=
operator|new
name|Integer
index|[
name|outputArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|str
init|=
name|outputArray
index|[
name|i
index|]
decl_stmt|;
name|outputFieldsIdxArray
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|fieldsLength
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|outputArray
init|=
name|fieldsLength
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|outputFieldsLengthArray
operator|=
operator|new
name|Integer
index|[
name|outputArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|outputArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|str
init|=
name|outputArray
index|[
name|i
index|]
decl_stmt|;
name|outputFieldsLengthArray
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// AS400 connections
comment|/**      * Obtains an {@code AS400} object that connects to this endpoint. Since      * these objects represent limited resources, clients have the      * responsibility of {@link #releaseConnection(AS400) releasing them} when      * done.      *       * @return an {@code AS400} object that connects to this endpoint      */
DECL|method|getConnection ()
specifier|public
name|AS400
name|getConnection
parameter_list|()
block|{
name|AS400
name|system
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting an AS400 object for '{}' from {}."
argument_list|,
name|systemName
operator|+
literal|'/'
operator|+
name|userID
argument_list|,
name|connectionPool
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isSecured
argument_list|()
condition|)
block|{
name|system
operator|=
name|connectionPool
operator|.
name|getSecureConnection
argument_list|(
name|systemName
argument_list|,
name|userID
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|system
operator|=
name|connectionPool
operator|.
name|getConnection
argument_list|(
name|systemName
argument_list|,
name|userID
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ccsid
operator|!=
name|DEFAULT_SYSTEM_CCSID
condition|)
block|{
name|system
operator|.
name|setCcsid
argument_list|(
name|ccsid
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|system
operator|.
name|setGuiAvailable
argument_list|(
name|guiAvailable
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyVetoException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to disable AS/400 prompting in the environment running Camel. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|system
return|;
comment|// Not null here.
block|}
catch|catch
parameter_list|(
name|ConnectionPoolException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unable to obtain an AS/400 connection for system name '%s' and user ID '%s'"
argument_list|,
name|systemName
argument_list|,
name|userID
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PropertyVetoException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unable to set the CSSID to use with "
operator|+
name|system
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Releases a previously obtained {@code AS400} object from use.      *       * @param connection a previously obtained {@code AS400} object to release      */
DECL|method|releaseConnection (AS400 connection)
specifier|public
name|void
name|releaseConnection
parameter_list|(
name|AS400
name|connection
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connection
argument_list|,
literal|"connection"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|connectionPool
operator|.
name|returnConnectionToPool
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

