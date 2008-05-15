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
name|ParseException
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

begin_comment
comment|/**  * Atom utilities.  */
end_comment

begin_class
DECL|class|AtomUtils
specifier|public
specifier|final
class|class
name|AtomUtils
block|{
DECL|method|AtomUtils ()
specifier|private
name|AtomUtils
parameter_list|()
block|{
comment|// Helper class
block|}
comment|/**      * Gets the Atom parser.      */
DECL|method|getAtomParser ()
specifier|public
specifier|static
name|Parser
name|getAtomParser
parameter_list|()
block|{
return|return
name|Abdera
operator|.
name|getInstance
argument_list|()
operator|.
name|getParser
argument_list|()
return|;
block|}
comment|/**      * Parses the given uri and returns the response as a atom feed document.      *      * @param uri the uri for the atom feed.      * @return  the document      * @throws IOException is thrown if error reading from the uri      * @throws ParseException is thrown if the parsing failed      */
DECL|method|parseDocument (String uri)
specifier|public
specifier|static
name|Document
argument_list|<
name|Feed
argument_list|>
name|parseDocument
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseException
block|{
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
name|getAtomParser
argument_list|()
operator|.
name|parse
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

