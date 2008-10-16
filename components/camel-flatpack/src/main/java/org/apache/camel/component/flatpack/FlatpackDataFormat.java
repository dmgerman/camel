begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DataSet
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DefaultParserFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|ParserFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|writer
operator|.
name|DelimiterWriterFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|writer
operator|.
name|FixedWriterFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|writer
operator|.
name|Writer
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
name|spi
operator|.
name|DataFormat
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

begin_import
import|import
name|org
operator|.
name|jdom
operator|.
name|JDOMException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * Flatpack DataFormat.  *<p/>  * This data format supports two operations:  *<ul>  *<li>marshal = from<tt>List&lt;Map&lt;String, Object&gt;&gt;</tt> to<tt>OutputStream</tt> (can be converted to String)</li>  *<li>unmarshal = from<tt>InputStream</tt> (such as a File) to {@link DataSetList}.  *</ul>  *<b>Notice:</b> The Flatpack library does currently not support header and trailers for the marshal operation.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|FlatpackDataFormat
specifier|public
class|class
name|FlatpackDataFormat
implements|implements
name|DataFormat
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
name|FlatpackDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|parserFactory
specifier|private
name|ParserFactory
name|parserFactory
init|=
name|DefaultParserFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
DECL|field|delimiter
specifier|private
name|char
name|delimiter
init|=
literal|','
decl_stmt|;
DECL|field|textQualifier
specifier|private
name|char
name|textQualifier
init|=
literal|'"'
decl_stmt|;
DECL|field|ignoreFirstRecord
specifier|private
name|boolean
name|ignoreFirstRecord
init|=
literal|true
decl_stmt|;
DECL|field|definition
specifier|private
name|Resource
name|definition
decl_stmt|;
DECL|field|fixed
specifier|private
name|boolean
name|fixed
decl_stmt|;
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|graph
argument_list|,
literal|"The object to marshal must be provided"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
operator|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
operator|)
name|graph
decl_stmt|;
if|if
condition|(
name|data
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No data to marshal as the list is empty"
argument_list|)
expr_stmt|;
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|firstRow
init|=
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Writer
name|writer
init|=
name|createWriter
argument_list|(
name|exchange
argument_list|,
name|firstRow
argument_list|,
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
name|writer
operator|.
name|printHeader
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
range|:
name|data
control|)
block|{
if|if
condition|(
name|ignoreFirstRecord
operator|&&
name|first
condition|)
block|{
comment|// skip first row
name|first
operator|=
literal|false
expr_stmt|;
continue|continue;
block|}
for|for
control|(
name|String
name|key
range|:
name|row
operator|.
name|keySet
argument_list|()
control|)
block|{
name|writer
operator|.
name|addRecordEntry
argument_list|(
name|key
argument_list|,
name|row
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|nextRecord
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|printFooter
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStreamReader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|)
decl_stmt|;
try|try
block|{
name|Parser
name|parser
init|=
name|createParser
argument_list|(
name|exchange
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|DataSet
name|dataSet
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
return|return
operator|new
name|DataSetList
argument_list|(
name|dataSet
argument_list|)
return|;
block|}
finally|finally
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|isFixed ()
specifier|public
name|boolean
name|isFixed
parameter_list|()
block|{
return|return
name|fixed
return|;
block|}
DECL|method|setFixed (boolean fixed)
specifier|public
name|void
name|setFixed
parameter_list|(
name|boolean
name|fixed
parameter_list|)
block|{
name|this
operator|.
name|fixed
operator|=
name|fixed
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|char
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (char delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|char
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|isIgnoreFirstRecord ()
specifier|public
name|boolean
name|isIgnoreFirstRecord
parameter_list|()
block|{
return|return
name|ignoreFirstRecord
return|;
block|}
DECL|method|setIgnoreFirstRecord (boolean ignoreFirstRecord)
specifier|public
name|void
name|setIgnoreFirstRecord
parameter_list|(
name|boolean
name|ignoreFirstRecord
parameter_list|)
block|{
name|this
operator|.
name|ignoreFirstRecord
operator|=
name|ignoreFirstRecord
expr_stmt|;
block|}
DECL|method|getTextQualifier ()
specifier|public
name|char
name|getTextQualifier
parameter_list|()
block|{
return|return
name|textQualifier
return|;
block|}
DECL|method|setTextQualifier (char textQualifier)
specifier|public
name|void
name|setTextQualifier
parameter_list|(
name|char
name|textQualifier
parameter_list|)
block|{
name|this
operator|.
name|textQualifier
operator|=
name|textQualifier
expr_stmt|;
block|}
DECL|method|getDefinition ()
specifier|public
name|Resource
name|getDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
DECL|method|setDefinition (Resource definition)
specifier|public
name|void
name|setDefinition
parameter_list|(
name|Resource
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|getParserFactory ()
specifier|public
name|ParserFactory
name|getParserFactory
parameter_list|()
block|{
return|return
name|parserFactory
return|;
block|}
DECL|method|setParserFactory (ParserFactory parserFactory)
specifier|public
name|void
name|setParserFactory
parameter_list|(
name|ParserFactory
name|parserFactory
parameter_list|)
block|{
name|this
operator|.
name|parserFactory
operator|=
name|parserFactory
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|createParser (Exchange exchange, Reader bodyReader)
specifier|protected
name|Parser
name|createParser
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Reader
name|bodyReader
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isFixed
argument_list|()
condition|)
block|{
name|Resource
name|resource
init|=
name|getDefinition
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|resource
argument_list|,
literal|"resource property"
argument_list|)
expr_stmt|;
return|return
name|getParserFactory
argument_list|()
operator|.
name|newFixedLengthParser
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
name|bodyReader
argument_list|)
return|;
block|}
else|else
block|{
name|Resource
name|resource
init|=
name|getDefinition
argument_list|()
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
return|return
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getParserFactory
argument_list|()
operator|.
name|newDelimitedParser
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
name|bodyReader
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|,
name|ignoreFirstRecord
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|createWriter (Exchange exchange, Map<String, Object> firstRow, OutputStream stream)
specifier|private
name|Writer
name|createWriter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|firstRow
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|JDOMException
throws|,
name|IOException
block|{
if|if
condition|(
name|isFixed
argument_list|()
condition|)
block|{
name|Resource
name|resource
init|=
name|getDefinition
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|resource
argument_list|,
literal|"resource property"
argument_list|)
expr_stmt|;
name|FixedWriterFactory
name|factory
init|=
operator|new
name|FixedWriterFactory
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|Resource
name|resource
init|=
name|getDefinition
argument_list|()
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|DelimiterWriterFactory
name|factory
init|=
operator|new
name|DelimiterWriterFactory
argument_list|(
name|delimiter
argument_list|,
name|textQualifier
argument_list|)
decl_stmt|;
comment|// add coulmns from the keys in the data map as the columns must be known
for|for
control|(
name|String
name|key
range|:
name|firstRow
operator|.
name|keySet
argument_list|()
control|)
block|{
name|factory
operator|.
name|addColumnTitle
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
operator|.
name|createWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|DelimiterWriterFactory
name|factory
init|=
operator|new
name|DelimiterWriterFactory
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
name|delimiter
argument_list|,
name|textQualifier
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

