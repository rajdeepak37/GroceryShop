package com.imaginej {

package snippet {

import net.liftweb.http.S
import S._
  
object I10N {

  def i10n(key: String, args: Object*) = {
    import java.text.{FieldPosition, MessageFormat}
    val formatter = new MessageFormat(S.?(key), S.locale)
    formatter.format(args.toArray, new StringBuffer, new FieldPosition(0)).toString
  }

}
}

}

