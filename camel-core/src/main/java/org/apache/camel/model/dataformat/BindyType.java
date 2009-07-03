begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_comment
comment|/**  * Represents the different types of bindy data formats.  *  * @version $Revision$  */
end_comment

begin_enum
annotation|@
name|XmlType
annotation|@
name|XmlEnum
argument_list|(
name|String
operator|.
name|class
argument_list|)
DECL|enum|BindyType
specifier|public
enum|enum
name|BindyType
block|{
DECL|enumConstant|Csv
DECL|enumConstant|KeyValue
name|Csv
block|,
name|KeyValue
block|}
end_enum

end_unit

